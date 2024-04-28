package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.databinding.FragmentNutritionDetailBinding

private const val TAG = "NutritionDetailFragment"

class NutritionDetailFragment : Fragment() {
    private val args: NutritionDetailFragmentArgs by navArgs()

    private lateinit var nutrition: Nutrition

    private var _binding: FragmentNutritionDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

//    private val args: NutritionDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nutritionDate = args.nutritionDate

//        Log.d(TAG, "The nutrition ID is: ${args.nutritionId}")
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

        binding.breakfastAddButton.setOnClickListener {
            navigateToSearchFragment()
        }

        binding.lunchAddButton.setOnClickListener {
            navigateToSearchFragment()
        }

        binding.dinnerAddButton.setOnClickListener {
            navigateToSearchFragment()
        }

        binding.snacksAddButton.setOnClickListener {
            navigateToSearchFragment()
        }

        binding.apply {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    private fun navigateToSearchFragment() {
        findNavController().navigate(NutritionDetailFragmentDirections.actionNutritionDetailFragmentToNutritionSearchFragment())
    }


}