package com.example.strawhats_workouttracker.ui.nutrition

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.databinding.FragmentNutritionFactsBinding
import java.text.DecimalFormat

class NutritionFactsFragment : Fragment() {

    private var _binding: FragmentNutritionFactsBinding? = null
    private val binding get() = _binding!!

    private val decimalFormat = DecimalFormat("#.##")

    private val args: NutritionFactsFragmentArgs by navArgs()

    private var selectedUnit: String = "grams"

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
        updateUIWithFoodItem(foodItem)

        val options = arrayOf("grams", "ounces", "pounds")
        val spinner: Spinner = binding.spinner

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedUnit = options[position]
                // Update labels based on the selected unit
                updateLabelsBasedOnUnit()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        binding.editWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(s: Editable?) {
                // Calculate nutrition based on the entered quantity and selected unit
                calculateNutrition(foodItem)
            }
        })
    }

    private fun calculateNutrition(foodItem: FoodItem) {
        val quantity = binding.editWeight.text.toString().toFloatOrNull() ?: return

        // Convert quantity to grams if necessary
        val quantityInGrams = when (selectedUnit) {
            "ounces" -> quantity * 28.3495f
            "pounds" -> quantity * 453.592f
            else -> quantity
        }

        // Calculate nutrition based on quantity and unit
        val nutritionInfo = calculateNutritionInfo(foodItem, quantityInGrams)

        // Update UI with new nutrition information
        updateUIWithNutritionInfo(nutritionInfo)
    }

    private fun calculateNutritionInfo(foodItem: FoodItem, quantityInGrams: Float): FoodItem {
        val adjustedFoodItem = FoodItem(
            foodItem.name,
            foodItem.calories * (quantityInGrams / foodItem.serving_size_g),
            foodItem.serving_size_g,
            foodItem.fat_total_g * (quantityInGrams / foodItem.serving_size_g),
            foodItem.protein_g * (quantityInGrams / foodItem.serving_size_g),
            foodItem.carbohhydrates_total_g * (quantityInGrams / foodItem.serving_size_g)
        )

        return adjustedFoodItem
    }

    private fun updateUIWithFoodItem(foodItem: FoodItem) {
        binding.textFoodName.text = "Food: ${foodItem.name}"
        binding.textFoodCalories.text = "Calories: ${foodItem.calories} g"
        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(foodItem.serving_size_g)} g"
        binding.textFoodFatTotalG.text = "Total Fat: ${foodItem.fat_total_g} g"
        binding.textFoodProteinG.text = "Protein: ${foodItem.protein_g} g"
        binding.textCarbohydratesTotalG.text = "Carbohydrates: ${foodItem.carbohhydrates_total_g} g"
    }

    private fun updateUIWithNutritionInfo(foodItem: FoodItem) {
        val servingLabel = when (selectedUnit) {
            "grams" -> "g"
            "ounces" -> "oz"
            "pounds" -> "lbs"
            else -> "g"
        }

        binding.textFoodCalories.text = "Calories: ${decimalFormat.format(foodItem.calories)} g"
        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(foodItem.serving_size_g)} $servingLabel"
        binding.textFoodFatTotalG.text = "Total Fat: ${decimalFormat.format(foodItem.fat_total_g)} g"
        binding.textFoodProteinG.text = "Protein: ${decimalFormat.format(foodItem.protein_g)} g"
        binding.textCarbohydratesTotalG.text = "Carbohydrates: ${decimalFormat.format(foodItem.carbohhydrates_total_g)} g"
    }

    private fun updateLabelsBasedOnUnit() {
        val servingLabel = when (selectedUnit) {
            "grams" -> "g"
            "ounces" -> "oz"
            "pounds" -> "lbs"
            else -> "g"
        }

        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(binding.textFoodServingSizeG.text.toString().split(" ")[2].toDouble())} $servingLabel"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}