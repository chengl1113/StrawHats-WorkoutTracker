package com.example.strawhats_workouttracker.ui.nutrition

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentNutritionListBinding
import com.example.strawhats_workouttracker.databinding.FragmentWorkoutBinding
import com.example.strawhats_workouttracker.ui.workout.WorkoutAdapter
import com.example.strawhats_workouttracker.ui.workout.WorkoutFragmentDirections
import java.time.LocalDate
import java.util.Date
import java.util.UUID

private const val TAG = "NutritionListFragment"
class NutritionListFragment : Fragment() {

    // get userId
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId : String


    private var _binding: FragmentNutritionListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val nutritionViewModel: NutritionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "none").toString()
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionListBinding.inflate(inflater, container, false)

        binding.nutritionRecyclerView.layoutManager = LinearLayoutManager(context)

        val newNutrition = Nutrition(
            LocalDate.now(),
            calories = 0.0,
            breakfast = mutableListOf<FoodItem>(),
            lunch = mutableListOf<FoodItem>(),
            dinner = mutableListOf<FoodItem>(),
            snacks = mutableListOf<FoodItem>()
        )
        binding.newDayButton.setOnClickListener {
            nutritionViewModel.addNutrition(newNutrition)
            findNavController().navigate(NutritionListFragmentDirections.showNutritionDetail(newNutrition))
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nutritionViewModel.userId = userId
        nutritionViewModel.nutritions.observe(viewLifecycleOwner) {nutritions ->
            Log.d(TAG, "nutritions: $nutritions")
            val adapter = NutritionListAdapter(nutritions) { nutrition ->
                val action = NutritionListFragmentDirections.showNutritionDetail(nutrition)
                findNavController().navigate(action)
            }
            binding.nutritionRecyclerView.adapter = adapter
        }
    }

//        val nutritions = nutritionViewModel.nutritions
//        val adapter = NutritionListAdapter(nutritions){nutritionDate ->
//            findNavController().navigate(
//                NutritionListFragmentDirections.showNutritionDetail(nutritionDate)
//            )
//        }
//        binding.nutritionRecyclerView.adapter = adapter


    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_account -> {
                // Navigate to settings screen.
                findNavController().navigate(R.id.nutrition_list_to_accountFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}