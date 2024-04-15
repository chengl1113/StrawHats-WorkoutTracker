package com.example.strawhats_workouttracker.ui.workout

data class Exercise(
    val name: String,
    val sets: MutableList<Set>
) {
    override fun toString(): String {
        return "name: $name | sets: ${sets.toString()}"
    }
}
