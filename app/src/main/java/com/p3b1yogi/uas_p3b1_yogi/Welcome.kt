package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.p3b1yogi.uas_p3b1_yogi.databinding.ActivityWelcomeBinding

class Welcome : AppCompatActivity() {
    // inisiasi binding
    private lateinit var binding: ActivityWelcomeBinding

    // memanggil aktivitas yang dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        // menetapkan tampiplan ui untuk activity sesuai binding.root
        setContentView(binding.root)

        //fungsi onClick untuk tombol welcomeBtn
        binding.welcomeBtn.setOnClickListener {
            //intent untuk memulai aktivitas tab_layout
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
        }

    }
}