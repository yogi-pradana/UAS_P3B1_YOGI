package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.p3b1yogi.uas_p3b1_yogi.databinding.ActivityHomeAdminBinding

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var itemAdapter: CalonAdminAdapter
    private lateinit var itemList: ArrayList<CalonAdminData>
    private lateinit var recyclerViewItem : RecyclerView

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inflate layout untuk activity admin home
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inisialisasi recyclerview dan adapter untuk daftar calon admin
        recyclerViewItem = binding.rvFilm
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()
        itemAdapter = CalonAdminAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

        // menambahkan listener ke tombol calon baru
        binding.btnPlusAdmin.setOnClickListener{
            startActivity(Intent(this,AdminAddActivity::class.java))
        }

        // menambahkan listener ke tombol kembali
        binding.buttonBack.setOnClickListener {
            // intent ke halaman login
            startActivity(Intent(this@HomeAdminActivity, LoginRegisterActivity::class.java))
            Firebase.auth.signOut()
            // menutup aktivitas saat ini untuk mencegah kembali ke aktivitas ini dengan tombol kembali
            finish()
        }

        // inisialisasi referensi database firebase
        database = FirebaseDatabase.getInstance().getReference("Film")

        // menambahkan ValueEventListener untuk mendengarkan perubahan data di Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // mengosongkan daftar film admin
                itemList.clear()

                // mendapatkan data dari firebase
                for(dataSnapshot in snapshot.children){
                    val item = dataSnapshot.getValue(CalonAdminData::class.java)
                    if(item != null){
                        // menambahkan data ke daftar film admin
                        itemList.add(item)
                    }
                }

                // memberitahu adapter bahwa data telah berubah
                itemAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // menampilkan pesan kesalahan jika pengambilan data gagal
                Toast.makeText(this@HomeAdminActivity, "Data retrieval failed!", Toast.LENGTH_SHORT).show()
            }
        })

    }
}