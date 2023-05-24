package com.example.chlorophyll.viewmodels

import androidx.lifecycle.*
import com.example.chlorophyll.data.Plant
import com.example.chlorophyll.data.PlantDao
import com.example.chlorophyll.data.SettingsDataStore
import com.example.chlorophyll.domain.eventsFromPlants
import com.example.chlorophyll.util.extensions.calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.IllegalArgumentException
import kotlinx.coroutines.launch
import java.util.Calendar

typealias Window = Pair<Calendar, Calendar>
typealias Event = Plant
typealias DayEvents = MutableList<Event>
typealias Schedule = MutableMap<Calendar, DayEvents>

class ChlorophyllViewModel(
    private val plantDao: PlantDao,
    dataStore: SettingsDataStore
) : ViewModel() {

    private val plantsFlow = plantDao.getAll()

    private val specFlow = dataStore.windowPreference

    private val scheduleFlow = combine(
        plantsFlow,
        specFlow
    ) { plants, spec ->
        return@combine Calendar.getInstance().eventWindow(spec).run {
            eventsFromPlants(plants, this)
        }
    }
    val schedule = scheduleFlow
        .flowOn(Dispatchers.Default)
        .conflate()
        .asLiveData()

    val spec = specFlow.asLiveData()
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
    private val plantDao: PlantDao,
    private val dataStore: SettingsDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChlorophyllViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChlorophyllViewModel(plantDao, dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}