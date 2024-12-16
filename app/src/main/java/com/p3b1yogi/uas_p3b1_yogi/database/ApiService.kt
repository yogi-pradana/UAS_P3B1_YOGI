package com.p3b1yogi.uas_p3b1_yogi.api

import com.p3b1yogi.uas_p3b1_yogi.database.Calon
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("calon_tabel")
    fun getCalons(): Call<List<Calon>>
}