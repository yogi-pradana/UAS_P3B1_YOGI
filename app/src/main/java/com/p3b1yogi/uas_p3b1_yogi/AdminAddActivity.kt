package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.p3b1yogi.uas_p3b1_yogi.databinding.ActivityAdminAddBinding
import java.util.UUID

class AdminAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddBinding

    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    // menerima hasil untuk mendapatkan konten gambar dari penyimpanan perangkat
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imgViewAdd.setImageURI(uri)
                // Optionally, you can call uploadData(imageUri) here if needed
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // menangani penambahan data ketika tombol "Add" ditekan
        binding.btnAdd.setOnClickListener {
            uploadData(imageUri)
        }

        // menangani pemilihan gambar ketika tombol "Choose Image" ditekan
        binding.btnChooseImage.setOnClickListener {
            getContent.launch("image/*")
        }

        // menangani kembali ke HomeAdminActivity saat tombol kembali ditekan
        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, HomeAdminActivity::class.java)
            startActivity(intent)
            finish() //
        }
    }

    // fungsi untuk mengunggah data film baru ke Firebase Database dan Firebase Storage
    private fun uploadData(imageUri: Uri? = null) {
        // mendapatkan nilai dari formulir
        val name: String = binding.txtName.text.toString()
        val age: String = binding.txtAge.text.toString()
        val hobby: String = binding.txtHobby.text.toString()
        val deskripsi: String = binding.txtDeskripsi.text.toString()

        // membuat ID unik untuk gambar
        val imageId = UUID.randomUUID().toString()

        if (name.isNotEmpty() && age.isNotEmpty() && hobby.isNotEmpty() && deskripsi.isNotEmpty() && imageUri != null) {
            // jika semua kolom formulir diisi dan gambar dipilih, unggah gambar ke Firebase Storage

            // upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // jika unggah gambar berhasil, dapatkan URL unduhan gambar
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    // buat objek CalonAdminData dengan nilai dari formulir dan URL gambar baru
                    val item = CalonAdminData(name, age, hobby, deskripsi, imageUrl.toString())

                    // mendapatkan referensi ke Firebase Database
                    database = FirebaseDatabase.getInstance().getReference("Film")

                    // menyimpan data baru ke firebase database
                    database.child(imageId).setValue(item)
                        .addOnCompleteListener {

                            // engosongkan kolom formulir setelah berhasil unggah data
                            binding.txtName.text!!.clear()
                            binding.txtAge.text!!.clear()
                            binding.txtHobby.text!!.clear()
                            binding.txtDeskripsi.text!!.clear()

                            // menampilkan pesan data berhasil di up
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()

                            Log.d("URIIII",imageUri.toString())
                            Log.d("URIIII",imageUri.toString())
                            Log.d("URIIII",imageUri.toString())
                            Log.d("URIIII",imageUri.toString())
                            Log.d("URIIII",imageUri.toString())

                            // navigasi ke HomeAdminActivity
                            val intent = Intent(this, HomeAdminActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            // menampilkan pesan gagal jika ada kesalahan
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                // menampilkan pesan gagal jika unggah gambar gagal
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {

            // menampilkan pesan jika ada kolom formulir yang belum diisi atau gambar tidak dipilih
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }
}