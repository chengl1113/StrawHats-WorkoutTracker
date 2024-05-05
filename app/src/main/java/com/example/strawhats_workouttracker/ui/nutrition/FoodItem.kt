package com.example.strawhats_workouttracker.ui.nutrition

import java.io.Serializable


data class FoodItem(
    val name: String,
    var calories: Double,
    var serving_size_g: Double,
    var fat_total_g: Double,
//    val fat_saturated_g: Double,
    var protein_g: Double,
//    val sodium_mg: Int,
//    val potassium_mg: Int,
//    val cholesterol_mg: Int,
    var carbohydrates_total_g: Double,
//    val fiber_g: Double,
//    val sugar_g: Double
): Serializable {
    override fun toString(): String {
        return "Name: $name\n" +
                "Calories: $calories\n" +
                "Serving: $serving_size_g\n" +
                "Fat: $fat_total_g\n" +
                "Protein: $protein_g\n" +
                "Carbohydrates: $carbohydrates_total_g\n"
    }
}