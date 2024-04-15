package com.example.strawhats_workouttracker.ui.workout

data class Set(
    val weight: Int,
    val reps: Int,
    val rpe: Float
) {
    override fun toString(): String {
        return "weight: $weight | reps: $reps | rpe: $rpe"
    }
}
