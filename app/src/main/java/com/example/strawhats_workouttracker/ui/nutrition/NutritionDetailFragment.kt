package com.example.strawhats_workouttracker.ui.nutrition

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

private const val TAG = "NutritionDetailFragment"

class NutritionDetailFragment : Fragment() {
    private val args: NutritionDetailFragmentArgs by navArgs()

    private var _binding: FragmentNutritionDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nutrition = args.nutrition
        binding.nutritionCaloriesTextView.text = String.format("Logged: %.0f calories", nutrition.calories)

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