package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.strawhats_workouttracker.databinding.FragmentNutritionSearchBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date
import kotlin.math.log


private const val TAG = "NutritionSearchFragment"
class NutritionSearchFragment : Fragment() {

    private var _binding: FragmentNutritionSearchBinding? = null
    private val binding get() = _binding!!
    private val nutritionRepository = NutritionRepository()
    private val args: NutritionSearchFragmentArgs by navArgs()
    private var foodItems = listOf<FoodItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nutrition = args.nutrition
        val mealType = args.mealType

        binding.searchNutrition.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Perform search when query submitted
                query?.let { searchFood(it, nutrition, mealType) }
                Log.d(TAG, "foodItems: $foodItems")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed
                return false
            }
        })
    }

    private fun searchFood(query: String, nutrition: Nutrition, mealType: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = nutritionRepository.searchFood(
                    apiKey = "5nc99VjCqmPdbrMiwewmkQ==RBcTMBvNAqgA6KQC",
                    query = query
                )
                val selectedFoodItem = response.firstOrNull()
                selectedFoodItem?.let {
                    val action = NutritionSearchFragmentDirections.actionNutritionSearchFragmentToNutritionFactsFragment(selectedFoodItem, nutrition, mealType)
                    findNavController().navigate(action)
                }
                Log.d(TAG, "Response received: $response")
                // Handle response as needed
            } catch (e: Exception) {
                Log.e(TAG, "Error occurred: ${e.message}", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}