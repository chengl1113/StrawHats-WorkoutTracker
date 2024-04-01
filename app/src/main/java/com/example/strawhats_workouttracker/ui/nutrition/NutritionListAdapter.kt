package com.example.strawhats_workouttracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.Nutrition
import com.example.strawhats_workouttracker.databinding.ListItemNutritionBinding

class NutritionHolder(
    private val binding: ListItemNutritionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(nutrition: Nutrition) {
        binding.nutritionTitle.text = nutrition.title
        binding.nutritionDate.text = nutrition.date.toString()
        binding.nutritionCalories.text = "Calories: ${nutrition.calories}"

        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "${nutrition.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

class NutritionListAdapter(
    private val nutritions: List<Nutrition>
) : RecyclerView.Adapter<NutritionHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : NutritionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNutritionBinding.inflate(inflater, parent, false)
        return NutritionHolder(binding)
    }
    override fun onBindViewHolder(holder: NutritionHolder, position: Int) {
        val nutrition = nutritions[position]
        holder.bind(nutrition)

    }
    override fun getItemCount() = nutritions.size
}