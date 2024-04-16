package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.databinding.FragmentNutritionFactsBinding

class NutritionFactsFragment : Fragment() {

    private var _binding: FragmentNutritionFactsBinding? = null
    private val binding get() = _binding!!

    private val args: NutritionFactsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionFactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodItem = args.selectedFoodItem

        // Populate the layout with information from the FoodItem object
        binding.textFoodName.text = "Food: ${foodItem.name}"
        binding.textFoodCalories.text = "Calories: ${foodItem.calories} g"
        binding.textFoodServingSizeG.text = "Serving size: ${foodItem.serving_size_g} g"
        binding.textFoodFatTotalG.text = "Total Fat: ${foodItem.fat_total_g} g"
        binding.textFoodProteinG.text = "Protein: ${foodItem.protein_g} g"
        binding.textCarbohydratesTotalG.text = "Carbohydrates: ${foodItem.carbohhydrates_total_g} g"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}