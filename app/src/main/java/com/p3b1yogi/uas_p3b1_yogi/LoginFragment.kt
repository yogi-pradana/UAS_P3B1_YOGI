package com.p3b1yogi.uas_p3b1_yogi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.p3b1yogi.uas_p3b1_yogi.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    // Firebase Authentication dan Firestore untuk autentikasi dan database
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // SharedPreferences untuk menyimpan status login
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi view binding
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi FirebaseAuth dan Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // SharedPreferences untuk status login
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        binding.loginBtn.setOnClickListener {
            // Ambil data dari input pengguna
            val email = binding.email.text.toString().trim()
            val password = binding.pass.text.toString().trim()

            // Validasi input
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Login menggunakan Firebase Authentication
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Ambil data role pengguna dari Firestore
                            val userId = firebaseAuth.currentUser!!.uid
                            fetchUserRoleFromFirestore(userId)
                        } else {
                            // Tampilkan pesan jika login gagal
                            Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Tampilkan pesan jika ada kolom kosong
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserRoleFromFirestore(userId: String) {
        // Ambil data pengguna dari Firestore berdasarkan userId
        firestore.collection("accounts").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role") ?: "user"
                    navigateToHomeOrAdmin(role)
                } else {
                    Toast.makeText(requireContext(), "User data not found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Tampilkan pesan jika gagal mengambil data
                Log.e("LoginFragment", "Error fetching user role", e)
                Toast.makeText(requireContext(), "Failed to fetch user role: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToHomeOrAdmin(role: String) {
        // Navigasi ke halaman admin atau user berdasarkan role
        val intent = if (role == "admin") {
            Intent(requireContext(), HomeAdminActivity::class.java)
        } else {
            Intent(requireContext(), BottomNavigationActivity::class.java)
        }
        startActivity(intent)
        requireActivity().finish()
    }
}
