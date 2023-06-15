package com.dicoding.autisdetection.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: EntityResult)

    @Query("SELECT * FROM EntityResult ORDER BY id ASC")
    suspend fun getAllResult(): List<EntityResult>
}