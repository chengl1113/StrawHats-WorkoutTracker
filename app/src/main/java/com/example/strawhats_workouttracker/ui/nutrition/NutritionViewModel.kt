package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.strawhats_workouttracker.ui.workout.Workout
import com.example.strawhats_workouttracker.ui.workout.WorkoutRepository
import java.util.Date
import java.util.UUID

class NutritionViewModel : ViewModel() {
//    val nutritions = mutableListOf<Nutrition>()
//    init {
//        for (i in 0 until 100) {
//            val nutrition = Nutrition(
//                id = UUID.randomUUID(),
//                title ="Nutrition #$i",
//                date = Date(),
//                calories = 0
//            )
//            nutritions += nutrition
//        }
//    }
    var userId = "-NvBHWxO-OSvtZEFgnbN"


    private val _nutritions = MutableLiveData<List<Nutrition>>()
    val nutritions: LiveData<List<Nutrition>> = _nutritions

    private val nutritionRepository = NutritionDatabaseRepository(userId)

    init {
        fetchNutritions()
    }

    fun addNutrition(nutrition: Nutrition) {
        // First, update the LiveData to update the UI immediately.
        val updatedList = _nutritions.value?.toMutableList() ?: mutableListOf()
        updatedList.add(nutrition)
        _nutritions.value = updatedList

        // Then, add the nutrition data to Firebase to store it persistently.
        nutritionRepository.addNutrition(nutrition)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNutrition(nutrition: Nutrition) {
        // First, update the LiveData to update the UI immediately.
        val updatedList = _nutritions.value?.toMutableList() ?: mutableListOf()
        val index = updatedList.indexOfFirst { it.date == nutrition.date }
        if (index != -1) {
            updatedList[index] = nutrition
            _nutritions.value = updatedList
        }

        // Then, update the nutrition data in Firebase to store it persistently.
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
