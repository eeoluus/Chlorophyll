package com.example.chlorophyll.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(plant: Plant)

    @Query("SELECT * FROM plant_table WHERE id = :key")
    fun get(key: Int): Flow<Plant>

    @Query("SELECT * FROM plant_table WHERE name = :name")
    fun getByName(name: String): Flow<Plant>

    @Query("SELECT * FROM plant_table")
    fun getAll(): Flow<List<Plant>>

    @Update
    suspend fun update(plant: Plant)

    @Delete
    suspend fun delete(plant: Plant)

    @Query("DELETE FROM plant_table WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Query("DELETE FROM plant_table")
    suspend fun deleteAll()
}