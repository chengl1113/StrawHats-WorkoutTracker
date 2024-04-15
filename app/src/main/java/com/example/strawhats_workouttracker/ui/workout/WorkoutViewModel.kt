package com.example.strawhats_workouttracker.ui.workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class WorkoutViewModel : ViewModel() {

    var workouts = mutableListOf<Workout>()

    init {
        for (i in 0 until 20) {
            val workout = Workout(
                date = LocalDate.now(),
                duration = Random.nextInt(60),
                exercises = mutableListOf(
                    Exercise("Bench press", mutableListOf(
                        Set(315, 5, 8f),
                        Set(250, 4, 7f),
                        Set(225, 6, 9f)
                    )),
                    Exercise("Chest fly", mutableListOf(
                        Set(117, 7, 8f),
                        Set(150, 5, 9f),
                        Set(150, 6, 10f)
                    )),
                    Exercise("Dips", mutableListOf(
                        Set(190, 11, 8f),
                        Set(200, 8, 7f),
                        Set(200, 6, 9f)
                    ))
                )
            )
            workouts += workout
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is workout Fragment"
    }
    val text: LiveData<String> = _text
}