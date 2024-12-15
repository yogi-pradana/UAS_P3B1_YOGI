package com.p3b1yogi.uas_p3b1_yogi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Calon::class], version = 1, exportSchema = false)
abstract class CalonRoomDb : RoomDatabase() {
    abstract fun localDao() : CalonDao?

    companion object {
        @Volatile
        private var INSTANCE: CalonRoomDb? = null
        fun getDatabase(context: Context): CalonRoomDb? {
            if (INSTANCE == null) {
                synchronized(CalonRoomDb::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        CalonRoomDb::class.java,
                        "Lovecation_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}