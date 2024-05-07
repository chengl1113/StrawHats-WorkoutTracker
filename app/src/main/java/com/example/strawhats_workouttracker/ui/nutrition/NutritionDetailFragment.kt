package com.example.strawhats_workouttracker.ui.nutrition

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.databinding.FragmentNutritionDetailBinding
import java.text.DecimalFormat

private const val TAG = "NutritionDetailFragment"

class NutritionDetailFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var goalCalories: String
    private val args: NutritionDetailFragmentArgs by navArgs()

    private val decimalFormat = DecimalFormat("#,###")

    private var _binding: FragmentNutritionDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val nutrition = args.nutrition
        binding.nutritionCaloriesTextView.text = "Logged: ${decimalFormat.format(nutrition.calories)} kcal"
        binding.goalCalorieText.text = "Goal: ${decimalFormat.format(goalCalories.toDouble())} kcal"

        setupRecyclerView(binding.breakfastRecyclerView, nutrition.breakfast)
        setupRecyclerView(binding.lunchRecyclerView, nutrition.lunch)
        setupRecyclerView(binding.dinnerRecyclerView, nutrition.dinner)
        setupRecyclerView(binding.snacksRecyclerView, nutrition.snacks)

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

        binding.apply {

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