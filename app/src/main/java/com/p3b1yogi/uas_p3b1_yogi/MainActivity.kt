package com.p3b1yogi.uas_p3b1_yogi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi FirebaseAuth dan Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Periksa apakah pengguna sudah login
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // Ambil data akun dari Firestore untuk menentukan role
            checkUserRole(currentUser.uid)
        } else {
            // Jika belum login, arahkan ke LoginRegisterActivity
            navigateToLogin()
        }
    }

    /**
     * Ambil role pengguna dari Firestore berdasarkan userId
     * @param userId ID pengguna dari Firebase Authentication
     */
    private fun checkUserRole(userId: String) {
        firestore.collection("accounts").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role") ?: "user"
                    navigateToHome(role)
                } else {
                    // Jika dokumen pengguna tidak ditemukan, logout dan arahkan ke login
                    Toast.makeText(this, "User data not found, please register.", Toast.LENGTH_SHORT).show()
                    logoutUser()
                }
            }
            .addOnFailureListener { exception ->
                // Jika ada kesalahan jaringan atau server, tampilkan pesan
                Toast.makeText(
                    this,
                    "Failed to fetch user data: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                logoutUser()
            }
    }

    /**
     * Navigasi berdasarkan role pengguna (admin atau user)
     * @param role Role pengguna ('admin' atau 'user')
     */
    private fun navigateToHome(role: String) {
        val intent = if (role == "admin") {
            Intent(this, HomeAdminActivity::class.java)
        } else {
            Intent(this, BottomNavigationActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    /**
     * Navigasikan ke LoginRegisterActivity
     */
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginRegisterActivity::class.java))
        finish()
    }

    /**
     * Logout pengguna jika terjadi kesalahan atau data tidak valid
     */
    private fun logoutUser() {
        firebaseAuth.signOut()
        navigateToLogin()
    }
}
