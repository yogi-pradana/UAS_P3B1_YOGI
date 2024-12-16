package com.p3b1yogi.uas_p3b1_yogi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.p3b1yogi.uas_p3b1_yogi.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    // Inisialisasi view binding
    private lateinit var binding: FragmentRegisterBinding

    // Firebase Authentication dan Firestore untuk autentikasi dan database
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // SharedPreferences untuk menyimpan status login
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi FirebaseAuth dan Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // SharedPreferences untuk status login
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Klik tombol registrasi
        binding.regisBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val username = binding.username.text.toString().trim()
            val phone = binding.phone.text.toString().trim()
            val password = binding.pass.text.toString().trim()

            // Menentukan role berdasarkan switch
            val role = if (binding.roleSwitch.isChecked) "admin" else "user"  // Admin jika switch aktif

            // Validasi input pengguna
            if (email.isNotEmpty() && username.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                // Registrasi menggunakan Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser!!.uid
                            saveUserDataToFirestore(userId, email, username, phone, role)
                        } else {
                            // Tampilkan pesan error jika registrasi gagal
                            Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Tampilkan pesan error jika ada kolom kosong
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserDataToFirestore(userId: String, email: String, username: String, phone: String, role: String) {
        // Simpan data pengguna ke koleksi "accounts" di Firestore
        val user = hashMapOf(
            "email" to email,
            "username" to username,
            "phone" to phone,
            "role" to role  // Menyimpan role pengguna (admin atau user)
        )

        firestore.collection("accounts").document(userId)
            .set(user)
            .addOnSuccessListener {
                // Navigasi ke halaman sesuai role setelah berhasil menyimpan
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                navigateToHomeOrAdmin(role)
            }
            .addOnFailureListener { e ->
                // Tampilkan pesan error jika gagal menyimpan data ke Firestore
                Toast.makeText(requireContext(), "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToHomeOrAdmin(role: String) {
        // Menentukan halaman tujuan berdasarkan role
        val intent = if (role == "admin") {
            Intent(requireContext(), HomeAdminActivity::class.java)  // Halaman Admin
        } else {
            Intent(requireContext(), BottomNavigationActivity::class.java)  // Halaman User
        }
        startActivity(intent)
        requireActivity().finish()  // Menutup aktivitas registrasi setelah navigasi
    }
}
