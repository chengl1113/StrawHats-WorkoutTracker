package com.example.strawhats_workouttracker.ui.workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "WorkoutViewModel"
@RequiresApi(Build.VERSION_CODES.O)
class WorkoutViewModel() : ViewModel() {

    var userId = "-NvBHWxO-OSvtZEFgnbN"

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val workoutRepository = WorkoutRepository(userId)

    init {
        fetchWorkouts()
    }

    fun addWorkout(workout: Workout) {
        val updatedList = _workouts.value?.toMutableList() ?: mutableListOf()
        updatedList.add(workout)
        _workouts.value = updatedList
    }

    private fun fetchWorkouts() {
        workoutRepository.getWorkouts { fetchedWorkouts ->
            _workouts.postValue(fetchedWorkouts)
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is workout Fragment"
    }
    val text: LiveData<String> = _text
}