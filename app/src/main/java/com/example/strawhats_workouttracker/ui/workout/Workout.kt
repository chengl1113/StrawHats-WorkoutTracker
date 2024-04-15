package com.example.strawhats_workouttracker.ui.workout

import java.time.LocalDate

data class Workout(
    val date: LocalDate,
    var duration: Int,
    val exercises: MutableList<Exercise>
) {
    override fun toString(): String {
        return "Date: $date\nDuration: $duration\n Exercises: $exercises"
    }
}
