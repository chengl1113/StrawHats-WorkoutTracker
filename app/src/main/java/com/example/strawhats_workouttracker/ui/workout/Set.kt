package com.example.strawhats_workouttracker.ui.workout

import java.io.Serializable

data class Set(
    val weight: Int,
    val reps: Int,
    val rpe: Float
) : Serializable{
    override fun toString(): String {
        return "weight: $weight | reps: $reps | rpe: $rpe"
    }
}
