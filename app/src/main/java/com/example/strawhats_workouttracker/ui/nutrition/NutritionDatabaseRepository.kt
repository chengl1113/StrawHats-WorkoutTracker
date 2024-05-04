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

    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference: DatabaseReference = firebaseDatabase.getReference("users").child(userId).child("nutrition")

    fun getNutritions(callback: (MutableList<Nutrition>) -> Unit) {
        val nutritions = mutableListOf<Nutrition>()

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                // loop through each nutrition
                for (nutritionSnapshot in snapshot.children) {
                    val dayOfMonth = nutritionSnapshot.child("date").child("dayOfMonth").getValue(Int::class.java)
                    val month = nutritionSnapshot.child("date").child("monthValue").getValue(Int::class.java)
                    val year = nutritionSnapshot.child("date").child("year").getValue(Int::class.java)

                    val nutritionDate = LocalDate.of(year!!, month!!, dayOfMonth!!)
                    val nutritionCalories = nutritionSnapshot.child("calories").getValue(Double::class.java)
                    val breakfastFoodItems = mutableListOf<FoodItem>()
                    val lunchFoodItems = mutableListOf<FoodItem>()
                    val dinnerFoodItems = mutableListOf<FoodItem>()
                    val snacksFoodItems = mutableListOf<FoodItem>()

                    val breakfastRef = nutritionSnapshot.child("breakfast")
                    val lunchRef = nutritionSnapshot.child("lunch")
                    val dinnerRef = nutritionSnapshot.child("dinner")
                    val snacksRef = nutritionSnapshot.child("snacks")

                    // loop through each breakfast foodItem
                    for (foodItemSnapshot in breakfastRef.children) {
                        // get foodItem data
                        val foodItemName = foodItemSnapshot.child("name").getValue(String::class.java)
                        val foodItemCalories = foodItemSnapshot.child("calories").getValue(Double::class.java)
                        val foodItemServing = foodItemSnapshot.child("serving_size_g").getValue(Double::class.java)
                        val foodItemFat = foodItemSnapshot.child("fat_total_g").getValue(Double::class.java)
                        val foodItemProtein = foodItemSnapshot.child("protein_g").getValue(Double::class.java)
                        val foodItemCarbohydrates = foodItemSnapshot.child("carbohydrates_total_g").getValue(Double::class.java)

                        breakfastFoodItems += FoodItem(foodItemName!!, foodItemCalories!!, foodItemServing!!, foodItemFat!!, foodItemProtein!!, foodItemCarbohydrates!!)
                    }

                    // loop through each lunch foodItem
                    for (foodItemSnapshot in lunchRef.children) {
                        // get foodItem data
                        val foodItemName = foodItemSnapshot.child("name").getValue(String::class.java)
                        val foodItemCalories = foodItemSnapshot.child("calories").getValue(Double::class.java)
                        val foodItemServing = foodItemSnapshot.child("serving_size_g").getValue(Double::class.java)
                        val foodItemFat = foodItemSnapshot.child("fat_total_g").getValue(Double::class.java)
                        val foodItemProtein = foodItemSnapshot.child("protein_g").getValue(Double::class.java)
                        val foodItemCarbohydrates = foodItemSnapshot.child("carbohydrates_total_g").getValue(Double::class.java)

                        lunchFoodItems += FoodItem(foodItemName!!, foodItemCalories!!, foodItemServing!!, foodItemFat!!, foodItemProtein!!, foodItemCarbohydrates!!)
                    }

                    // loop through each dinner foodItem
                    for (foodItemSnapshot in dinnerRef.children) {
                        // get foodItem data
                        val foodItemName = foodItemSnapshot.child("name").getValue(String::class.java)
                        val foodItemCalories = foodItemSnapshot.child("calories").getValue(Double::class.java)
                        val foodItemServing = foodItemSnapshot.child("serving_size_g").getValue(Double::class.java)
                        val foodItemFat = foodItemSnapshot.child("fat_total_g").getValue(Double::class.java)
                        val foodItemProtein = foodItemSnapshot.child("protein_g").getValue(Double::class.java)
                        val foodItemCarbohydrates = foodItemSnapshot.child("carbohydrates_total_g").getValue(Double::class.java)

                        dinnerFoodItems += FoodItem(foodItemName!!, foodItemCalories!!, foodItemServing!!, foodItemFat!!, foodItemProtein!!, foodItemCarbohydrates!!)
                    }

                    // loop through each snacks foodItem
                    for (foodItemSnapshot in snacksRef.children) {
                        // get foodItem data
                        val foodItemName = foodItemSnapshot.child("name").getValue(String::class.java)
                        val foodItemCalories = foodItemSnapshot.child("calories").getValue(Double::class.java)
                        val foodItemServing = foodItemSnapshot.child("serving_size_g").getValue(Double::class.java)
                        val foodItemFat = foodItemSnapshot.child("fat_total_g").getValue(Double::class.java)
                        val foodItemProtein = foodItemSnapshot.child("protein_g").getValue(Double::class.java)
                        val foodItemCarbohydrates = foodItemSnapshot.child("carbohydrates_total_g").getValue(Double::class.java)

                        snacksFoodItems += FoodItem(foodItemName!!, foodItemCalories!!, foodItemServing!!, foodItemFat!!, foodItemProtein!!, foodItemCarbohydrates!!)
                    }

                    nutritions += Nutrition(nutritionDate!!, nutritionCalories!!,
                        breakfastFoodItems, lunchFoodItems, dinnerFoodItems, snacksFoodItems
                    )
                }

                callback(nutritions)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: DATABASE ERROR")
            }

        })
    }

    fun addNutrition(nutrition: Nutrition) {
        val newRef = databaseReference.push()  // Creates a new child with a unique key
        newRef.setValue(nutrition)
            .addOnSuccessListener {
                Log.d(TAG, "Nutrition added successfully!")
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to add nutrition", it)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNutrition(nutrition: Nutrition) {
        val dateKey = nutrition.date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        databaseReference.child(dateKey).setValue(nutrition)
            .addOnSuccessListener {
                Log.d("Firebase", "Nutrition updated successfully for date: $dateKey")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to update nutrition for date: $dateKey", e)
            }
    }
}