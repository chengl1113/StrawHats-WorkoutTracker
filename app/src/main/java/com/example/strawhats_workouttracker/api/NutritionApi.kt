package com.example.strawhats_workouttracker.api

import com.example.strawhats_workouttracker.ui.nutrition.FoodItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
interface NutritionApi {
    @GET("/v1/nutrition")
    suspend fun searchFood(
        @Query("query") query: String,
        @Header("X-Api-Key") apiKey: String
    ): List<FoodItem>
}
