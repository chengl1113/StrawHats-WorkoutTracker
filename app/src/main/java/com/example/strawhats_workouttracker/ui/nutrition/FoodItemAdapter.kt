package com.example.strawhats_workouttracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.ItemFoodBinding
import java.text.DecimalFormat

// ViewHolder class for managing the view of a single food item in a RecyclerView
class FoodItemHolder(
    private val binding: ItemFoodBinding
) : RecyclerView.ViewHolder(binding.root) {

    // Formatter to add commas in numbers
    private val decimalFormat = DecimalFormat("#,###")
    fun bind(foodItem: FoodItem) {
        // Displaying the food item's name, serving size, and calories
        binding.textViewFoodName.text = foodItem.name
        binding.textViewServing.text = "Serving: ${decimalFormat.format(foodItem.serving_size_g)}g"
        binding.textViewCalories.text = "${decimalFormat.format(foodItem.calories)} kcal"

    }
}

// Adapter class for RecyclerView to manage a list of food items
class FoodItemAdapter(
    private val foodItems: List<FoodItem>
) : RecyclerView.Adapter<FoodItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemHolder {
        // Inflate the layout for each item
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(inflater, parent, false)
        return FoodItemHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemHolder, position: Int) {
        val foodItem = foodItems[position]
        // Call bind to update the contents of the itemView at a given position
        holder.bind(foodItem)

        // Animation for the itemView when it is being bound
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation))
    }

    override fun getItemCount(): Int = foodItems.size
}