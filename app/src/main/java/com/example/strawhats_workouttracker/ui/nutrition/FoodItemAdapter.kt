package com.example.strawhats_workouttracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.databinding.ItemFoodBinding

class FoodItemHolder(
    private val binding: ItemFoodBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(foodItem: FoodItem) {
        // Manually setting the text for each TextView
        binding.textViewFoodName.text = foodItem.name
        binding.textViewServing.text = String.format("Serving: %.0f g", foodItem.serving_size_g)
        binding.textViewCalories.text = String.format("%.0f kcal", foodItem.calories)

    }
}

class FoodItemAdapter(
    private val foodItems: List<FoodItem>
) : RecyclerView.Adapter<FoodItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(inflater, parent, false)
        return FoodItemHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemHolder, position: Int) {
        val foodItem = foodItems[position]
        holder.bind(foodItem)
    }

    override fun getItemCount(): Int = foodItems.size
}