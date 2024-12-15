package com.p3b1yogi.uas_p3b1_yogi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.p3b1yogi.uas_p3b1_yogi.databinding.ActivityLoginRegisterBinding

class LoginRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // menggunakan view binding untuk meng-inflate layout
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inisialisasi SharedPreferences untuk menyimpan data secara lokal
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // memeriksa apakah pengguna sudah masuk
        val currentUser = Firebase.auth.currentUser

        // menyiapkan viewpager2 dan tablayout
        val adapter = LoginRegisAdapter(this@LoginRegisterActivity)
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->

            // menetapkan teks tab berdasarkan posisi
            tab.text = when (position) {
                0 -> "Login"
                1 -> "Register"
                else -> ""
            }
        }.attach()
    }
}