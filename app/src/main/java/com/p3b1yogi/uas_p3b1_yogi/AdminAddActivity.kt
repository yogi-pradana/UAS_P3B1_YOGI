package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.p3b1yogi.uas_p3b1_yogi.api.RetrofitInstance
import com.p3b1yogi.uas_p3b1_yogi.database.Calon
import com.p3b1yogi.uas_p3b1_yogi.database.CalonDao
import com.p3b1yogi.uas_p3b1_yogi.database.CalonRoomDb
import com.p3b1yogi.uas_p3b1_yogi.databinding.ActivityAdminAddBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class AdminAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var calonDao: CalonDao

    // menerima hasil untuk mendapatkan konten gambar dari penyimpanan perangkat
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            binding.imgViewAdd.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Room Database
        val db = CalonRoomDb.getDatabase(this)
        calonDao = db!!.localDao()!!

        // Mengambil data dari API dan menyimpan ke Room
        fetchCalonsFromApi()

        // Menangani tombol "Add"
        binding.btnAdd.setOnClickListener {
            uploadData(imageUri)
        }

        // Menangani tombol "Choose Image"
        binding.btnChooseImage.setOnClickListener {
            getContent.launch("image/*")
        }

        // Tombol kembali ke HomeAdminActivity
        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, HomeAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Fungsi untuk mengambil data dari API
    private fun fetchCalonsFromApi() {
        val apiService = RetrofitInstance.apiService

        apiService.getCalons().enqueue(object : Callback<List<Calon>> {
            override fun onResponse(call: Call<List<Calon>>, response: Response<List<Calon>>) {
                if (response.isSuccessful) {
                    response.body()?.let { calons ->
                        saveToRoomDatabase(calons)
                    }
                } else {
                    Toast.makeText(
                        this@AdminAddActivity,
                        "Failed to fetch data: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Calon>>, t: Throwable) {
                Toast.makeText(this@AdminAddActivity, "API Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Menyimpan data dari API ke Room Database
    private fun saveToRoomDatabase(calons: List<Calon>) {
        Thread {
            calons.forEach { calon ->
                val calonWithId = calon.copy(id = 0) // Menyetel id sebagai 0 untuk auto-generate
                calonDao.insert(calonWithId)
            }
            runOnUiThread {
                Toast.makeText(this, "Data saved to local database", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    // Fungsi untuk mengunggah data film baru ke Firebase Database dan Firebase Storage
    private fun uploadData(imageUri: Uri? = null) {
        val name: String = binding.txtName.text.toString()
        val age: String = binding.txtAge.text.toString()
        val hobby: String = binding.txtHobby.text.toString()
        val deskripsi: String = binding.txtDeskripsi.text.toString()

        val imageId = UUID.randomUUID().toString()

        if (name.isNotEmpty() && age.isNotEmpty() && hobby.isNotEmpty() && deskripsi.isNotEmpty() && imageUri != null) {
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = Calon(
                        id = 0,                          // Auto-generate ID
                        namaCalon = name,
                        ageCalon = age,
                        hobbyCalon = hobby,
                        deskripsiCalon = deskripsi,
                        imgCalon = imageUrl.toString()
                    )

                    database = FirebaseDatabase.getInstance().getReference("Film")
                    database.child(imageId).setValue(item)
                        .addOnCompleteListener {
                            binding.txtName.text!!.clear()
                            binding.txtAge.text!!.clear()
                            binding.txtHobby.text!!.clear()
                            binding.txtDeskripsi.text!!.clear()
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeAdminActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }
}
