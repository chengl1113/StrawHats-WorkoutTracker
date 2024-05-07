package com.example.strawhats_workouttracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.ItemFoodBinding
import java.text.DecimalFormat

class FoodItemHolder(
    private val binding: ItemFoodBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val decimalFormat = DecimalFormat("#,###")
    fun bind(foodItem: FoodItem) {
        // Manually setting the text for each TextView
        binding.textViewFoodName.text = foodItem.name
        binding.textViewServing.text = "Serving: ${decimalFormat.format(foodItem.serving_size_g)}g"
        binding.textViewCalories.text = "${decimalFormat.format(foodItem.calories)} kcal"

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

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation))
    }

    override fun getItemCount(): Int = foodItems.size
}