package com.example.strawhats_workouttracker

import java.util.Date

data class Workout(
    val date: Date,
    val exercises: MutableMap<String, MutableList<String>>
)
