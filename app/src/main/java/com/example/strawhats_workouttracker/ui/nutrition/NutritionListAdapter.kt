package com.example.strawhats_workouttracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.databinding.ListItemNutritionBinding
import java.util.Date
import java.util.UUID

class NutritionHolder(
    private val binding: ListItemNutritionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(nutrition: Nutrition, onNutritionClicked: (nutritionDate: Date) -> Unit) {
        binding.nutritionTitle.text = nutrition.title
        binding.nutritionDate.text = nutrition.date.toString()
        binding.nutritionCalories.text = "Calories: ${nutrition.calories}"

        binding.root.setOnClickListener {
            onNutritionClicked(nutrition.date)
        }
    }
}

class NutritionListAdapter(
    private val nutritions: List<Nutrition>,
    private val onNutritionClicked: (nutritionDate: Date) -> Unit
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
        holder.bind(nutrition, onNutritionClicked)

    }
    override fun getItemCount() = nutritions.size
}