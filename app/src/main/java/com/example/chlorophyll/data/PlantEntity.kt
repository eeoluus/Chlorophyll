package com.example.chlorophyll.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "plant_table")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "start_time")
    val date: Calendar,

    @ColumnInfo(name = "interval")
    val interval: Int,

    @ColumnInfo(name = "color")
    val color: String
)