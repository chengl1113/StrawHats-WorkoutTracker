package com.example.strawhats_workouttracker.ui.nutrition

import java.io.Serializable
import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class Nutrition(
    val date: LocalDate,
    var calories: Double,
    val breakfast: MutableList<FoodItem>,
    val lunch: MutableList<FoodItem>,
    val dinner: MutableList<FoodItem>,
    val snacks: MutableList<FoodItem>
) : Serializable {
    override fun toString(): String {
        return "Date: $date\nCalories: $calories\n Breakfast: $breakfast\n" +
                "Lunch: $lunch\n" +
                "Dinner: $dinner\n" +
                "Snacks: $snacks\n"
    }
}
