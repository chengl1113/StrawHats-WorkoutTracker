package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NutritionViewModel : ViewModel() {
    var userId = "-NvBHWxO-OSvtZEFgnbN"

    // LiveData that holds a list of Nutrition
    private val _nutritions = MutableLiveData<List<Nutrition>>()
    val nutritions: LiveData<List<Nutrition>> = _nutritions

    private val nutritionRepository = NutritionDatabaseRepository(userId)

    init {
        fetchNutritions()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNutrition(nutrition: Nutrition) {
        // Update LiveData with the new nutrition object
        val updatedList = _nutritions.value?.toMutableList() ?: mutableListOf()
        updatedList.add(nutrition)
        _nutritions.value = updatedList

        // Update nutrition data in the repository as well
        nutritionRepository.updateNutrition(nutrition)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNutrition(nutrition: Nutrition) {
        // Update LiveData list
        val updatedList = _nutritions.value?.toMutableList() ?: mutableListOf()
        val index = updatedList.indexOfFirst { it.date == nutrition.date }
        if (index != -1) {
            updatedList[index] = nutrition
            _nutritions.value = updatedList
        }

        // Then, update the nutrition data in Firebase
        nutritionRepository.updateNutrition(nutrition)
    }

    private fun fetchNutritions() {
        nutritionRepository.getNutritions { fetchedNutritions ->
            _nutritions.postValue(fetchedNutritions)
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is nutrition Fragment"
    }
    val text: LiveData<String> = _text
}
