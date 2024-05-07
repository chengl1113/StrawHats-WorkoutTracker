package com.example.strawhats_workouttracker.ui.nutrition

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.ListItemNutritionBinding
import java.text.DecimalFormat

// ViewHolder class for each nutrition item in recycler view
class NutritionHolder(
    private val binding: ListItemNutritionBinding,
    private val goal: Int,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private val decimalFormat = DecimalFormat("#,###")
    @SuppressLint("SetTextI18n")
    fun bind(nutrition: Nutrition, onNutritionClicked: (Nutrition) -> Unit) {
        binding.nutritionDate.text = nutrition.date.toString()
        binding.nutritionCalories.text = "${decimalFormat.format(nutrition.calories)} kcal"

        binding.root.setOnClickListener {
            onNutritionClicked(nutrition)
        }

        // Change color to red if user has consumed more calories than the goal
        if (nutrition.calories > goal) {
            binding.cardContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
    }
}

// Adapter class for recycler view to display a list of Nutrition objects
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

        // Animation for item view when it is being bound
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation))

    }
    override fun getItemCount() = nutritions.size
}