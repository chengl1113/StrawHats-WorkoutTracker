package com.example.strawhats_workouttracker

import java.util.Date
import java.util.UUID

data class Nutrition(
    val id: UUID,
    val title: String,
    val date: Date,
    val calories: Int
)
