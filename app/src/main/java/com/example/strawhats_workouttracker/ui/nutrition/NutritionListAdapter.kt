package com.example.strawhats_workouttracker.ui.nutrition

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.ListItemNutritionBinding
import java.text.DecimalFormat

class NutritionHolder(
    private val binding: ListItemNutritionBinding,
    private val goal: Int,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private val decimalFormat = DecimalFormat("#,###")
    fun bind(nutrition: Nutrition, onNutritionClicked: (Nutrition) -> Unit) {
        binding.nutritionDate.text = nutrition.date.toString()
        binding.nutritionCalories.text = "${decimalFormat.format(nutrition.calories)} kcal"

        binding.root.setOnClickListener {
            onNutritionClicked(nutrition)
        }
        if (nutrition.calories > goal) {
            binding.cardContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
    }
}

class NutritionListAdapter(
    private val nutritions: List<Nutrition>,
    private val goal: Int,
    private val onNutritionClicked: (Nutrition) -> Unit
) : RecyclerView.Adapter<NutritionHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : NutritionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNutritionBinding.inflate(inflater, parent, false)
        return NutritionHolder(binding, goal, parent.context)
    }
    override fun onBindViewHolder(holder: NutritionHolder, position: Int) {
        val nutrition = nutritions[position]
        holder.bind(nutrition) {onNutritionClicked(nutrition)}

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation))

    }
    override fun getItemCount() = nutritions.size
}