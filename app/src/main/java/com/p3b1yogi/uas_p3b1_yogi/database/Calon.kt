package com.p3b1yogi.uas_p3b1_yogi.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName

@Entity(tableName = "calon_tabel")
data class Calon (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,  // ID dari Room (auto-generate)

    @SerializedName("nama_calon")
    @ColumnInfo(name = "namaCalon")
    val namaCalon: String,

    @SerializedName("age_calon")
    @ColumnInfo(name = "ageCalon")
    val ageCalon: String,

    @SerializedName("hobby_calon")
    @ColumnInfo(name = "hobbyCalon")
    val hobbyCalon: String,

    @SerializedName("deskripsi_calon")
    @ColumnInfo(name = "deskripsiCalon")
    val deskripsiCalon: String,

    @SerializedName("img_calon")
    @ColumnInfo(name = "imgCalon")
    val imgCalon: String? = null  // Nullable karena opsional di JSON
)
