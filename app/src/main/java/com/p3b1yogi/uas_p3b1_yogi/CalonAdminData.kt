package com.p3b1yogi.uas_p3b1_yogi

import java.io.Serializable

// Kelas `CalonAdminData` merepresentasikan data film pada bagian admin
// Implementasi Serializable memungkinkan objek dari kelas ini dapat dikirimkan antar aktivitas menggunakan Intent
// Setiap objek `FilmAdminData` memiliki properti yang mencakup informasi judul, sutradara, durasi, rating, sinopsis, dan URL gambar film
class CalonAdminData(
    val name : String?= null,
    val age : String?= null,
    val hobby : String?= null,
    val deskripsi : String?= null,
    val imageUrl : String?= null)