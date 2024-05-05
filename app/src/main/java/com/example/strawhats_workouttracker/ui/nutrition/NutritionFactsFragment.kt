package com.example.strawhats_workouttracker.ui.nutrition

import android.R
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.databinding.FragmentNutritionFactsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.time.LocalDate

private const val TAG = "NutritionFactsFragment"

class NutritionFactsFragment : Fragment() {

    private var _binding: FragmentNutritionFactsBinding? = null
    private val binding get() = _binding!!

    private val decimalFormat = DecimalFormat("#.##")

    private val args: NutritionFactsFragmentArgs by navArgs()

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

        val foodItem = args.foodItem

        // Populate the layout with information from the FoodItem object
        populateUIWithFoodItem(foodItem)

        val options = arrayOf("grams", "ounces", "pounds")
        val spinner: Spinner = binding.spinner

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedUnit = options[position]
                // Update labels based on the selected unit
                updateLabelsBasedOnUnit(foodItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }


        binding.editWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                // Calculate nutrition based on the entered quantity and selected unit
                calculateNutrition(foodItem)
            }
        })

        binding.addFoodButton.setOnClickListener {
            val nutrition = args.nutrition
            when (args.mealType) {
                "breakfast" -> {
                    nutrition.breakfast += FoodItem(foodItem.name, foodItem.calories, foodItem.serving_size_g, foodItem.fat_total_g, foodItem.protein_g, foodItem.carbohydrates_total_g)
                    // Update other breakfast related fields similarly
                }

                "lunch" -> {
                    nutrition.lunch += FoodItem(foodItem.name, foodItem.calories, foodItem.serving_size_g, foodItem.fat_total_g, foodItem.protein_g, foodItem.carbohydrates_total_g)
                    // Update other lunch related fields similarly
                }

                "dinner" -> {
                    nutrition.dinner += FoodItem(foodItem.name, foodItem.calories, foodItem.serving_size_g, foodItem.fat_total_g, foodItem.protein_g, foodItem.carbohydrates_total_g)
                    // Update other dinner related fields similarly
                }

                "snacks" -> {
                    nutrition.snacks += FoodItem(foodItem.name, foodItem.calories, foodItem.serving_size_g, foodItem.fat_total_g, foodItem.protein_g, foodItem.carbohydrates_total_g)
                    // Update other snacks related fields similarly
                }
//            val newNutrition = createNutrition()
//            viewModel.addNutrition(newNutrition)
            }
            viewModel.updateNutrition(nutrition)
        }
    }

    private fun calculateNutrition(foodItem: FoodItem) {
        foodItem.serving_size_g = binding.editWeight.text.toString().toDoubleOrNull() ?: return

        // Convert quantity to grams if necessary
        val quantityInGrams = when (selectedUnit) {
            "ounces" -> foodItem.serving_size_g * 28.3495f
            "pounds" -> foodItem.serving_size_g * 453.592f
            else -> foodItem.serving_size_g
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

        foodItem.calories = foodItem.calories * (quantityInGrams / foodItem.serving_size_g)
        foodItem.serving_size_g = quantityInGrams
        foodItem.fat_total_g = foodItem.fat_total_g * (quantityInGrams / foodItem.serving_size_g)
        foodItem.protein_g = foodItem.protein_g * (quantityInGrams / foodItem.serving_size_g)
        foodItem.carbohydrates_total_g = foodItem.carbohydrates_total_g * (quantityInGrams / foodItem.serving_size_g)

        return adjustedFoodItem
    }

    private fun populateUIWithFoodItem(foodItem: FoodItem) {
        binding.textFoodName.text = "Food: ${foodItem.name}"
        binding.textFoodCalories.text = "Calories: ${foodItem.calories} kcal"
        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(foodItem.serving_size_g)} g"
        binding.textFoodFatTotalG.text = "Total Fat: ${foodItem.fat_total_g} g"
        binding.textFoodProteinG.text = "Protein: ${foodItem.protein_g} g"
        binding.textCarbohydratesTotalG.text = "Carbohydrates: ${foodItem.carbohydrates_total_g} g"
    }

    private fun updateUIWithNutritionInfo(foodItem: FoodItem) {
        val servingLabel = when (selectedUnit) {
            "grams" -> "g"
            "ounces" -> "oz"
            "pounds" -> "lbs"
            else -> "g"
        }

        binding.textFoodCalories.text = "Calories: ${decimalFormat.format(foodItem.calories)} kcal"
        binding.textFoodServingSizeG.text = "Serving size: ${decimalFormat.format(foodItem.serving_size_g)} $servingLabel"
        binding.textFoodFatTotalG.text = "Total Fat: ${decimalFormat.format(foodItem.fat_total_g)} g"
        binding.textFoodProteinG.text = "Protein: ${decimalFormat.format(foodItem.protein_g)} g"
        binding.textCarbohydratesTotalG.text = "Carbohydrates: ${decimalFormat.format(foodItem.carbohydrates_total_g)} g"
    }

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

//    private fun navigateToFragmentB(foodItem: FoodItem) {
//        val action = NutritionFactsFragmentDirections.actionNutritionFactsToNutritionDetail()
//        findNavController().navigate(action)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}