package com.example.chlorophyll.viewmodels

import androidx.lifecycle.*
import com.example.chlorophyll.data.Plant
import com.example.chlorophyll.data.PlantDao
import com.example.chlorophyll.domain.eventsFromPlants
import com.example.chlorophyll.util.extensions.calendar.*
import kotlinx.coroutines.flow.map
import java.lang.IllegalArgumentException
import kotlinx.coroutines.launch
import java.util.Calendar

typealias DayEvents = MutableList<Event>
typealias Schedule = MutableMap<Calendar, DayEvents>

typealias Event = Plant

class ChlorophyllViewModel(private val plantDao: PlantDao) : ViewModel() {

    val schedule = plantDao
        .getAll()
        .map(::eventsFromPlants)
        .asLiveData()

    private fun insertPlant(plant: Plant) {
        viewModelScope.launch {
            plantDao.insert(plant)
        }
    }
    fun addNewPlant(name: String, start: Calendar, interval: Int, color: String) {
        val entry = Plant(
            name = name,
            date = start,
            interval = interval,
            color = color
        )
        insertPlant(entry)
    }
    /* fun retrievePlant(name: String): LiveData<Plant> {
        return plantDao.getByName(name).asLiveData()
    }
    fun deletePlantByName(name: String) {
        viewModelScope.launch {
            plantDao.deleteByName(name)
        }
    } */
    fun deleteAll() {
        viewModelScope.launch {
            plantDao.deleteAll()
        }
    }
}

class ChlorophyllViewModelFactory(
    private val plantDao: PlantDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChlorophyllViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChlorophyllViewModel(plantDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}