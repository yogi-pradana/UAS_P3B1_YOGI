package com.p3b1yogi.uas_p3b1_yogi.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calon_tabel")
data class Calon (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "namaCalon")
    val namaCalon: String,
    @ColumnInfo(name = "ageCalon")
    val ageCalon: String,
    @ColumnInfo(name = "hobbyCalon")
    val hobbyCalon: String,
    @ColumnInfo(name = "deskripsiCalon")
    val deskripsiCalon: String,
    @ColumnInfo(name = "imgCalon")
    val imgCalon: String
)
