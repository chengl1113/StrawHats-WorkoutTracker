package com.example.strawhats_workouttracker.ui.nutrition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.strawhats_workouttracker.Nutrition
import java.util.Date
import java.util.UUID

class NutritionViewModel : ViewModel() {
    val nutritions = mutableListOf<Nutrition>()
    init {
        for (i in 0 until 100) {
            val nutrition = Nutrition(
                id = UUID.randomUUID(),
                title ="Nutrition #$i",
                date = Date(),
                calories = 0
            )
            nutritions += nutrition
        }
    }
}
