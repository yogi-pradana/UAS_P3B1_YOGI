package com.p3b1yogi.uas_p3b1_yogi

// data class `CalonUserData` digunakan untuk merepresentasikan informasi film pada bagian pengguna (user)
// setiap properti pada data class ini merepresentasikan atribut dari sebuah film, seperti judul, sutradara
// durasi, rating, sinopsis, dan URL gambar
// data class ini digunakan untuk menyimpan data calon ketika diambil dari Firebase atau digunakan di bagian pengguna aplikasi
class CalonUserData(
    val name : String?= null,
    val age : String?= null,
    val hobby : String? = null,
    val deskripsi : String?= null,
    val imageUrl : String?= null,
)