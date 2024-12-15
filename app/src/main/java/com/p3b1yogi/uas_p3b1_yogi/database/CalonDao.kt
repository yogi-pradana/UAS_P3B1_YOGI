package com.p3b1yogi.uas_p3b1_yogi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CalonDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    fun insert(local: Calon)

    @Update
    fun update(local: Calon)

    @Query("DELETE FROM calon_table")
    fun truncateTable()

    @Query("SELECT * FROM calon_table")
    fun allPostsLocal(): LiveData<List<Calon>>
}