package com.p3b1yogi.uas_p3b1_yogi

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.p3b1yogi.uas_p3b1_yogi.database.Calon
import com.p3b1yogi.uas_p3b1_yogi.database.CalonDao
import com.p3b1yogi.uas_p3b1_yogi.database.CalonRoomDb
import com.p3b1yogi.uas_p3b1_yogi.databinding.FragmentHomeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [HomeFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
class HomeFragment : Fragment() {
    // inisiasi variabel yang digunakan
    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemAdapter: CalonUserAdapter
    // inisiasi araylist film user data
    private var itemList: ArrayList<CalonUserData> = ArrayList<CalonUserData>()
    private lateinit var recyclerViewItem: RecyclerView

    private lateinit var database: DatabaseReference
    private lateinit var mAuth : FirebaseAuth

    //Room
    private lateinit var mLocalDao: CalonDao
    private lateinit var executorService: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inisiasi firebaseauth
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser!!.email

        // inisiasilisasi room database
        executorService = Executors.newSingleThreadExecutor()
        val db = CalonRoomDb.getDatabase(requireContext())
        mLocalDao = db!!.localDao()!!

        // cek ketersediaan internet dan ambil data
        if (isInternetAvailable(requireActivity())) {
            fetchData()
            Toast.makeText(requireActivity(), "Establishing Connection", Toast.LENGTH_SHORT).show()
        }else{
            fetchDataOffline()
            Toast.makeText(requireActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

        // set username dari email pengguna
        binding.getUsername.setText(currentUser!!.substringBefore('@').toString())

        // inisialisasi recyclerview dan adapter
        recyclerViewItem = binding.rvCalon
        recyclerViewItem.setHasFixedSize(true)

        // menggunakan GridLayoutManager dengan jumlah rentang 2 untuk dua kolom
        recyclerViewItem.layoutManager = GridLayoutManager(requireContext(), 2)

        itemList = arrayListOf()
        itemAdapter = CalonUserAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

    }


    // function
    // fungsi untuk mereset isi tabel di database lokal
    private fun truncateTable() {
        executorService.execute { mLocalDao.truncateTable() }
    }

    // fungsi untuk menyisipkan data ke dalam database lokal
    private fun insert(local: Calon) {
        executorService.execute { mLocalDao.insert(local) }
    }

    // fungsi untuk memerika ketersediaan internet
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    // fungsi untuk mengambil data dari firebase
    private fun fetchData() {
        // mengosongkan tabel lokal
        truncateTable()

        // mendapatkan referensi database firebase
        database = FirebaseDatabase.getInstance().getReference("Film")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // mengososngkan daftar film
                itemList.clear()

                // mengambil data dari firebase
                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(CalonUserData::class.java)
                    if (item != null) {

                        // menambahkan data ke daftar film dan lokal
                        itemList.add(item)
                        val local = Calon(
                            namaCalon = item.name!!,
                            ageCalon = item.age!!,
                            hobbyCalon = item.hobby!!,

                            deskripsiCalon = item.deskripsi!!,
                            imgCalon = item.imageUrl!!
                        )
                        insert(local)
                    }
                }
                Log.d("NEWWW","${itemList.toString()}")
                Log.d("NEWWW","${itemList.toString()}")
                Log.d("NEWWW","${itemList.toString()}")

                // memberitahu adapter bahwa data telah berubah agar dia ikut berubah
                itemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // mengatasi kesalahan jika diperlukan
            }
        })
    }

    // fungsi untuk mengambil data dari database lokal jika tidak ada koneksi internet
    private fun fetchDataOffline() {

        // mengosongkan daftar calon
        itemList.clear()

        // mengambil data dari database lokal menggunakan Room
        mLocalDao.allPostsLocal().observe(requireActivity()) {movies ->
            for (movie in movies){
                // menambahkan data ke daftar film
                val local = CalonUserData(
                    name = movie.namaCalon,
                    age = movie.ageCalon,
                    hobby = movie.hobbyCalon,
                    deskripsi = movie.deskripsiCalon,
                    imageUrl = movie.imgCalon
                )
                itemList.add(local)
            }

            // memberitahu adapter bahwa data telah berubah
            itemAdapter.notifyDataSetChanged()
        }
    }
}