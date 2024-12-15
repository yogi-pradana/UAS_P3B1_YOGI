package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.p3b1yogi.uas_p3b1_yogi.databinding.ActivityDetailCalonBinding

// kelas `DetailCalonActivity` bertanggung jawab untuk menampilkan detail calon ketika pengguna memilih calo
class DetailCalonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCalonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCalonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // mendapatkan data dari intent yang memulai aktivitas ini dan menetapkannya ke tampilan detail
        binding.namaCalon.text = intent.getStringExtra("name")
        binding.ageCalon.text = intent.getStringExtra("age")
        binding.hobbyCalon.text = intent.getStringExtra("hobby")
        binding.deskripsiCalon.text = intent.getStringExtra("deskripsi")

        // menangani aksi ketika tombol kembali ditekan
        binding.back.setOnClickListener {
            // kembali ke halaman sebelumnya (HomeFragment) dan mengakhiri aktivitas saat ini
            startActivity(Intent(this@DetailCalonActivity, HomeFragment::class.java))
            finishAffinity()
        }

        // memuat gambar menggunakan Glide ke dalam ImageView
        val imageUrl = intent.getStringExtra("imageUrl")
        if (imageUrl != null) {
            Glide.with(binding.imageCalon)
                .load(imageUrl)
                .centerCrop()
                .into(binding.imageCalon)
        }
    }
}