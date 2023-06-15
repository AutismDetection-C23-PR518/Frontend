package com.dicoding.autisdetection.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntityResult::class], version = 2, exportSchema = false)
abstract class ResultDatabase : RoomDatabase() {

    abstract fun resultDao(): ResultDao

    companion object{
        @Volatile
        private var INSTANCE: ResultDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : ResultDatabase {
            if (INSTANCE == null) {
                synchronized(ResultDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ResultDatabase::class.java, "result_database")
                            .fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE as ResultDatabase
        }
    }


}