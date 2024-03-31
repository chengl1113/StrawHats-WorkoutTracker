package com.example.strawhats_workouttracker.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.strawhats_workouttracker.NutritionDate
import java.util.Date
import java.util.UUID

class WorkoutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is workout Fragment"
    }
    val text: LiveData<String> = _text
}