package com.example.strawhats_workouttracker.ui.nutrition

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentNutritionDetailBinding
import java.text.DecimalFormat

private const val TAG = "NutritionDetailFragment"

class NutritionDetailFragment : Fragment() {
    // User sharedPreferences and goal calories data
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var goalCalories: String
    private val args: NutritionDetailFragmentArgs by navArgs()

    // Formatter for numbers with commas
    private val decimalFormat = DecimalFormat("#,###")

    private var _binding: FragmentNutritionDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize sharedPreferences and get goal calories
        sharedPreferences = requireActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
        goalCalories = sharedPreferences.getInt("calorie goal", 0).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentNutritionDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display logged and goal calories
        val nutrition = args.nutrition
        binding.nutritionCaloriesTextView.text = "Logged: ${decimalFormat.format(nutrition.calories)} kcal"
        binding.goalCalorieText.text = "Goal: ${decimalFormat.format(goalCalories.toDouble())} kcal"

        binding.topCardView.setOnClickListener {
            if (binding.additionalInfoLayout.visibility == View.VISIBLE) {
                // Hide it
                binding.additionalInfoLayout.visibility = View.GONE
                binding.additionalInfoLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
                binding.expandMoreIcon.setImageResource(R.drawable.expand_more_24px)

            } else {
                // Show it
                binding.additionalInfoLayout.visibility = View.VISIBLE
                binding.additionalInfoLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
                binding.expandMoreIcon.setImageResource(R.drawable.expand_less_24px)
                // Calculate total protein for the day
                val protein = (nutrition.breakfast.sumOf { it.protein_g } +
                        nutrition.lunch.sumOf { it.protein_g } +
                        nutrition.dinner.sumOf { it.protein_g } +
                        nutrition.snacks.sumOf { it.protein_g })
                // Calculate total carbohydrates for the day
                val carbs = (nutrition.breakfast.sumOf { it.carbohydrates_total_g } +
                        nutrition.lunch.sumOf { it.carbohydrates_total_g } +
                        nutrition.dinner.sumOf { it.carbohydrates_total_g } +
                        nutrition.snacks.sumOf { it.carbohydrates_total_g })
                // Calculate total fat for the day
                val fat = (nutrition.breakfast.sumOf { it.fat_total_g } +
                        nutrition.lunch.sumOf { it.fat_total_g } +
                        nutrition.dinner.sumOf { it.fat_total_g } +
                        nutrition.snacks.sumOf { it.fat_total_g })
                // Display the information
                binding.nutritionTotalFatAdditionalTextView.text = "Fat: ${decimalFormat.format(fat)} g"
                binding.nutritionProteinAdditionalTextView.text = "Protein: ${decimalFormat.format(protein)} g"
                binding.nutritionCarbsAdditionalTextView.text = "Carbs: ${decimalFormat.format(carbs)} g"
            }
        }

        // Set up recycler views for the different meal types
        setupRecyclerView(binding.breakfastRecyclerView, nutrition.breakfast)
        setupRecyclerView(binding.lunchRecyclerView, nutrition.lunch)
        setupRecyclerView(binding.dinnerRecyclerView, nutrition.dinner)
        setupRecyclerView(binding.snacksRecyclerView, nutrition.snacks)

        // Navigate to next fragment with meal type as argument
        binding.breakfastAddButton.setOnClickListener {
            navigateToSearchFragment(nutrition, "breakfast")
        }
        binding.lunchAddButton.setOnClickListener {
            navigateToSearchFragment(nutrition, "lunch")
        }
        binding.dinnerAddButton.setOnClickListener {
            navigateToSearchFragment(nutrition, "dinner")
        }
        binding.snacksAddButton.setOnClickListener {
            navigateToSearchFragment(nutrition, "snacks")
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, foodItems: List<FoodItem>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = FoodItemAdapter(foodItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    private fun navigateToSearchFragment(nutrition: Nutrition, mealType: String) {
        findNavController().navigate(NutritionDetailFragmentDirections.actionNutritionDetailFragmentToNutritionSearchFragment(nutrition, mealType))
    }


}