package com.example.strawhats_workouttracker.ui.nutrition

import com.example.strawhats_workouttracker.api.NutritionApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NutritionRepository {
    private val nutritionApi: NutritionApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        nutritionApi = retrofit.create(NutritionApi::class.java)
    }

    suspend fun searchFood(apiKey: String, query: String): List<FoodItem> =
        nutritionApi.searchFood(query, apiKey)
}