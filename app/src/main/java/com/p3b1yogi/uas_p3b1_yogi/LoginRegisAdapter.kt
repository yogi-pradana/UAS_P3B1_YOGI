package com.p3b1yogi.uas_p3b1_yogi

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LoginRegisAdapter(act: AppCompatActivity) : FragmentStateAdapter(act) {
    // menentukan jumlah fragment dalam viewpager2
    override fun getItemCount(): Int {
        return 2
    }

    // membuat dan mengembalikan fragment yang sesuai dengan posisi
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // mengembalikan instance LoginFragment untuk posisi 0
            0 -> LoginFragment()
            // mengembalikan instance RegisterFragment untuk posisi 1
            1 -> RegisterFragment()
            // mengembalikan exception jika posisi tidak valid
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}