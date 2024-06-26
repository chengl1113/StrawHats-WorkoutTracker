package com.example.strawhats_workouttracker.ui.nutrition

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentNutritionFactsBinding
import java.text.DecimalFormat

class NutritionFactsFragment : Fragment() {

    private var _binding: FragmentNutritionFactsBinding? = null
    private val binding get() = _binding!!

    // Formatter for numbers with decimals and commas
    private val decimalFormat = DecimalFormat("#,###.##")

    private val args: NutritionFactsFragmentArgs by navArgs()

    // Unit for food item, default is grams
    private var selectedUnit: String = "grams"

    private val viewModel: NutritionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionFactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display the food item details
        val foodItem = args.foodItem
        populateUIWithFoodItem(foodItem)

        // Set up dropdown values
        val options = arrayOf("grams", "ounces", "pounds")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, options)
        val autoCompleteTextView = binding.autoCompleteTextView
        autoCompleteTextView.setAdapter(adapter)

        // Listener for unit selection
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            // Get the selected item using position
            selectedUnit = parent.adapter.getItem(position) as String
            // Update labels based on the selected unit
            updateLabelsBasedOnUnit(foodItem)
        }

        // Listener for changes in text of the weight field
        binding.editWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                // Calculate nutrition based on the entered quantity and selected unit
                calculateNutrition(foodItem)
            }
        })

        // Listener for the "Add" button
        binding.addFoodButton.setOnClickListener {
            // Construct the food item with the correct values based on weight and unit
            if (binding.editWeight.text.toString().toDoubleOrNull() != null) {
                val quantity = binding.editWeight.text.toString().toDouble()
                // Convert quantity to grams if necessary
                val quantityInGrams = when (selectedUnit) {
                    "ounces" -> quantity * 28.3495f
                    "pounds" -> quantity * 453.592f
                    else -> quantity
                }
                foodItem.calories = foodItem.calories * (quantityInGrams / foodItem.serving_size_g)
                foodItem.fat_total_g = foodItem.fat_total_g * (quantityInGrams / foodItem.serving_size_g)
                foodItem.protein_g = foodItem.protein_g * (quantityInGrams / foodItem.serving_size_g)
                foodItem.carbohydrates_total_g = foodItem.carbohydrates_total_g * (quantityInGrams / foodItem.serving_size_g)
                foodItem.serving_size_g = quantityInGrams
            }

            // Add the food item to the correct meal type
            val nutrition = args.nutrition
            when (args.mealType) {
                "breakfast" -> {
                    nutrition.breakfast += FoodItem(foodItem.name,
                        foodItem.calories,
                        foodItem.serving_size_g,
                        foodItem.fat_total_g,
                        foodItem.protein_g,
                        foodItem.carbohydrates_total_g)
                }

                "lunch" -> {
                    nutrition.lunch += FoodItem(foodItem.name,
                        foodItem.calories,
                        foodItem.serving_size_g,
                        foodItem.fat_total_g,
                        foodItem.protein_g,
                        foodItem.carbohydrates_total_g)
                }

                "dinner" -> {
                    nutrition.dinner += FoodItem(foodItem.name,
                        foodItem.calories,
                        foodItem.serving_size_g,
                        foodItem.fat_total_g,
                        foodItem.protein_g,
                        foodItem.carbohydrates_total_g)
                }

                "snacks" -> {
                    nutrition.snacks += FoodItem(foodItem.name,
                        foodItem.calories,
                        foodItem.serving_size_g,
                        foodItem.fat_total_g,
                        foodItem.protein_g,
                        foodItem.carbohydrates_total_g)
                }
            }

            // Calculate total calories and update the nutrition object
            val totalCalories = calculateTotalCalories(nutrition)
            nutrition.calories = totalCalories

            // Update the Nutrition object with this food item addition
            viewModel.updateNutrition(nutrition)

            // Clean the back stack for ease of navigation
            val action = NutritionFactsFragmentDirections.actionNutritionFactsToNutritionDetail(nutrition)
            findNavController().navigate(action)
            findNavController().popBackStack()
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }

    // Helper function to calculate the total calories for today
    private fun calculateTotalCalories(nutrition: Nutrition): Double {
        return (nutrition.breakfast.sumOf { it.calories } +
                nutrition.lunch.sumOf { it.calories } +
                nutrition.dinner.sumOf { it.calories } +
                nutrition.snacks.sumOf { it.calories })
    }

    // Helper function to calculate and update nutritional information to the UI
    private fun calculateNutrition(foodItem: FoodItem) {
        val quantity = binding.editWeight.text.toString().toDoubleOrNull() ?: return

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

    private fun calculateNutritionInfo(foodItem: FoodItem, quantityInGrams: Double): FoodItem {
        val adjustedFoodItem = FoodItem(
            foodItem.name,
            foodItem.calories * (quantityInGrams / foodItem.serving_size_g),
            binding.editWeight.text.toString().toDouble(),
            foodItem.fat_total_g * (quantityInGrams / foodItem.serving_size_g),
            foodItem.protein_g * (quantityInGrams / foodItem.serving_size_g),
            foodItem.carbohydrates_total_g * (quantityInGrams / foodItem.serving_size_g)
        )

        return adjustedFoodItem
    }

    // Display the initial information to the UI
    @SuppressLint("SetTextI18n")
    private fun populateUIWithFoodItem(foodItem: FoodItem) {
        binding.textFoodName.text = "Food: ${foodItem.name}"
        binding.textFoodCalories.text = "${decimalFormat.format(foodItem.calories)} kcal"
        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(foodItem.serving_size_g)} g"
        binding.textFoodFatTotalG.text = "Total Fat: ${decimalFormat.format(foodItem.fat_total_g)} g"
        binding.textFoodProteinG.text = "Protein: ${decimalFormat.format(foodItem.protein_g)} g"
        binding.textCarbohydratesTotalG.text = "Carbohydrates: ${decimalFormat.format(foodItem.carbohydrates_total_g)} g"
    }

    // Display the updated information to the UI
    @SuppressLint("SetTextI18n")
    private fun updateUIWithNutritionInfo(foodItem: FoodItem) {
        val servingLabel = when (selectedUnit) {
            "grams" -> "g"
            "ounces" -> "oz"
            "pounds" -> "lbs"
            else -> "g"
        }

        binding.textFoodCalories.text = "${decimalFormat.format(foodItem.calories)} kcal"
        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(foodItem.serving_size_g)} $servingLabel"
        binding.textFoodFatTotalG.text = "Total Fat: ${decimalFormat.format(foodItem.fat_total_g)} g"
        binding.textFoodProteinG.text = "Protein: ${decimalFormat.format(foodItem.protein_g)} g"
        binding.textCarbohydratesTotalG.text = "Carbohydrates: ${decimalFormat.format(foodItem.carbohydrates_total_g)} g"
    }

    @SuppressLint("SetTextI18n")
    private fun updateLabelsBasedOnUnit(foodItem: FoodItem) {
        val servingLabel = when (selectedUnit) {
            "grams" -> "g"
            "ounces" -> "oz"
            "pounds" -> "lbs"
            else -> "g"
        }
        calculateNutrition(foodItem)
        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(binding.textFoodServingSizeG.text.toString().split(" ")[2].toDouble())} $servingLabel"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}