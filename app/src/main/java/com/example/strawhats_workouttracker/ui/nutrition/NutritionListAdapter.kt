package com.example.strawhats_workouttracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.databinding.ListItemNutritionBinding

class NutritionHolder(
    private val binding: ListItemNutritionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(nutrition: Nutrition, onNutritionClicked: (Nutrition) -> Unit) {
        binding.nutritionDate.text = nutrition.date.toString()
        binding.nutritionCalories.text = String.format("%.0f kcal", nutrition.calories)

        binding.root.setOnClickListener {
            onNutritionClicked(nutrition)
        }
    }
}

class NutritionListAdapter(
    private val nutritions: List<Nutrition>,
    private val onNutritionClicked: (Nutrition) -> Unit
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
        holder.bind(nutrition) {onNutritionClicked(nutrition)}

    }
    override fun getItemCount() = nutritions.size
}