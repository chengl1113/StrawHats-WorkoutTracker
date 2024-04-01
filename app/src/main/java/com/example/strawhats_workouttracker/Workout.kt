package com.example.strawhats_workouttracker

import java.time.Duration
import java.util.Date

data class Workout(
    val date: Date,
    val duration: Duration,
    val exercises: MutableMap<String, MutableList<String>>
)
