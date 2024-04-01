package com.example.strawhats_workouttracker.ui.workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.strawhats_workouttracker.Workout
import java.time.Duration
import java.util.Date
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class WorkoutViewModel : ViewModel() {

    var workouts = mutableListOf<Workout>()

    init {
        for (i in 0 until 20) {
            val workout = Workout(
                date = Date(),
                duration = Duration.ofHours(1)
                    .plusMinutes(Random.nextInt(60).toLong()),
                exercises = mutableMapOf(
                    "Bench press" to mutableListOf("315x5 @ RPE 8", "250x4 @ RPE 7", "225x6 @ RPE 9"),
                    "Chest fly" to mutableListOf("115x7 @ RPE 8", "150x5 @ RPE 9", "150x6 @ RPE 10"),
                    "Dips" to mutableListOf("190x11 @ RPE 8", "200x8 @ RPE 7", "200x6 @ RPE 9")
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