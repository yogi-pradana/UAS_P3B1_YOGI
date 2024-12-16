package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.p3b1yogi.uas_p3b1_yogi.database.Calon
import com.p3b1yogi.uas_p3b1_yogi.database.CalonDao
import com.p3b1yogi.uas_p3b1_yogi.database.CalonRoomDb
import com.p3b1yogi.uas_p3b1_yogi.databinding.ActivityDetailCalonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailCalonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCalonBinding
    private lateinit var calonDao: CalonDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCalonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Room Database
        val db = CalonRoomDb.getDatabase(applicationContext)
        calonDao = db!!.localDao()!!

        // Ambil data dari intent dan tampilkan
        val name = intent.getStringExtra("name")
        val age = intent.getStringExtra("age")
        val hobby = intent.getStringExtra("hobby")
        val deskripsi = intent.getStringExtra("deskripsi")
        val imageUrl = intent.getStringExtra("imageUrl")

        binding.namaCalon.text = name
        binding.ageCalon.text = age
        binding.hobbyCalon.text = hobby
        binding.deskripsiCalon.text = deskripsi

        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(binding.imageCalon)

        // Tambahkan logika untuk tombol "Choose"
        binding.btnChoose.setOnClickListener {
            if (name != null && age != null && hobby != null && deskripsi != null && imageUrl != null) {
                saveCalonToFavorites(name, age, hobby, deskripsi, imageUrl)
            } else {
                Toast.makeText(this, "Invalid data!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol kembali
        binding.back.setOnClickListener {
            startActivity(Intent(this@DetailCalonActivity, HomeFragment::class.java))
            finishAffinity()
        }
    }

    private fun saveCalonToFavorites(
        name: String, age: String, hobby: String, deskripsi: String, imageUrl: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            // Buat objek Calon dan simpan ke Room Database
            val calon = Calon(
                namaCalon = name,
                ageCalon = age,
                hobbyCalon = hobby,
                deskripsiCalon = deskripsi,
                imgCalon = imageUrl
            )
            calonDao.insert(calon)

            // Tampilkan pesan sukses di thread utama
            runOnUiThread {
                Toast.makeText(this@DetailCalonActivity, "Calon added to favorites!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
