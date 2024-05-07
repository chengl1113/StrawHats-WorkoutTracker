package com.example.strawhats_workouttracker.ui.workout

import java.io.Serializable

data class Exercise(
    val name: String,
    val sets: MutableList<Set>
) : Serializable{
    override fun toString(): String {
        return "name: $name | sets: ${sets.toString()}"
    }
}
