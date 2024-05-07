package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "NutritionDatabaseRepository"

class NutritionDatabaseRepository(userId: String) {

    // Reference to the user nutrition data in Firebase database instance
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference: DatabaseReference = firebaseDatabase.getReference("users").child(userId).child("nutrition")

    // Fetch all nutrition data for the user and return as callback
    fun getNutritions(callback: (MutableList<Nutrition>) -> Unit) {
        val nutritions = mutableListOf<Nutrition>()

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                // Loop through each nutrition
                for (nutritionSnapshot in snapshot.children) {
                    val dayOfMonth = nutritionSnapshot.child("date").child("dayOfMonth").getValue(Int::class.java)
                    val month = nutritionSnapshot.child("date").child("monthValue").getValue(Int::class.java)
                    val year = nutritionSnapshot.child("date").child("year").getValue(Int::class.java)

                    // Store LocalDate and total calories of this day
                    val nutritionDate = LocalDate.of(year!!, month!!, dayOfMonth!!)
                    val nutritionCalories = nutritionSnapshot.child("calories").getValue(Double::class.java)

                    // Lists of FoodItem for different meal types
                    val breakfastFoodItems = mutableListOf<FoodItem>()
                    val lunchFoodItems = mutableListOf<FoodItem>()
                    val dinnerFoodItems = mutableListOf<FoodItem>()
                    val snacksFoodItems = mutableListOf<FoodItem>()

                    val breakfastRef = nutritionSnapshot.child("breakfast")
                    val lunchRef = nutritionSnapshot.child("lunch")
                    val dinnerRef = nutritionSnapshot.child("dinner")
                    val snacksRef = nutritionSnapshot.child("snacks")

                    // Extract and construct breakfast food items
                    extractFoodItems(breakfastRef, breakfastFoodItems)
                    // Extract and construct lunch food items
                    extractFoodItems(lunchRef, lunchFoodItems)
                    // Extract and construct dinner food items
                    extractFoodItems(dinnerRef, dinnerFoodItems)
                    // Extract and construct snacks food items
                    extractFoodItems(snacksRef, snacksFoodItems)

                    // Add Nutrition object to the running list of nutrition objects
                    nutritions += Nutrition(nutritionDate!!, nutritionCalories!!,
                        breakfastFoodItems, lunchFoodItems, dinnerFoodItems, snacksFoodItems
                    )
                }

                // Invoke the callback with the list of Nutrition objects
                callback(nutritions)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: DATABASE ERROR")
            }

        })
    }

    // Helper function to extract food items from a given snapshot
    private fun extractFoodItems(foodSnapshot: DataSnapshot, foodItems: MutableList<FoodItem>) {
        for (foodItemSnapshot in foodSnapshot.children) {
            val foodItemName = foodItemSnapshot.child("name").getValue(String::class.java)
            val foodItemCalories = foodItemSnapshot.child("calories").getValue(Double::class.java)
            val foodItemServing = foodItemSnapshot.child("serving_size_g").getValue(Double::class.java)
            val foodItemFat = foodItemSnapshot.child("fat_total_g").getValue(Double::class.java)
            val foodItemProtein = foodItemSnapshot.child("protein_g").getValue(Double::class.java)
            val foodItemCarbohydrates = foodItemSnapshot.child("carbohydrates_total_g").getValue(Double::class.java)

            // Add FoodItem data to list of specified meal type food items
            foodItems += FoodItem(foodItemName!!, foodItemCalories!!, foodItemServing!!, foodItemFat!!, foodItemProtein!!, foodItemCarbohydrates!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNutrition(nutrition: Nutrition) {
        val dateKey = nutrition.date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        // Update nutrition data in Firebase
        databaseReference.child(dateKey).setValue(nutrition)
            .addOnSuccessListener {
                Log.d("Firebase", "Nutrition updated successfully for date: $dateKey")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to update nutrition for date: $dateKey", e)
            }
    }
}