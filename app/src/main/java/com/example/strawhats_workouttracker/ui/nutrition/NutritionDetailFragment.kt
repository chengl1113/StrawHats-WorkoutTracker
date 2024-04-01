package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.Nutrition
import com.example.strawhats_workouttracker.databinding.FragmentNutritionDetailBinding
import java.util.Date
import java.util.UUID

private const val TAG = "NutritionDetailFragment"

class NutritionDetailFragment : Fragment() {

    private lateinit var nutrition: Nutrition

    private var _binding: FragmentNutritionDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: NutritionDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nutrition = Nutrition(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            calories = 0
        )
        Log.d(TAG, "The crime ID is: ${args.nutritionId}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentNutritionDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}