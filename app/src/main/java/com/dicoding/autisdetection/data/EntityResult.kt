package com.dicoding.autisdetection.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityResult (
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,

        @ColumnInfo(name = "result")
        val result: String,

        @ColumnInfo(name = "skor")
        val skor: String,
)