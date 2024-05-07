package com.example.strawhats_workouttracker.ui.nutrition

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.databinding.FragmentNutritionSearchBinding
import kotlinx.coroutines.launch


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

        // Set up listener for when the user hits enter or search
        binding.searchNutrition.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val searchText = v.text.toString()
                if (searchText.isNotEmpty()) {
                    // Perform the search
                    searchFood(searchText, nutrition, mealType)
                    Log.d(TAG, "Search submitted: $searchText")
                }
                // Hide the keyboard after searching
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    // Helper function for sending the query and navigating to the next fragment
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