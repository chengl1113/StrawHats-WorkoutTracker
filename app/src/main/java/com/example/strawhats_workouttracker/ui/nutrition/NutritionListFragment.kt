package com.example.strawhats_workouttracker.ui.nutrition

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentNutritionListBinding
import java.util.UUID

private const val TAG = "NutritionListFragment"
class NutritionListFragment : Fragment() {

    private var _binding: FragmentNutritionListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val nutritionViewModel: NutritionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(TAG, "Total nutritions: ${nutritionViewModel.nutritions.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNutritionListBinding.inflate(inflater, container, false)

        binding.nutritionRecyclerView.layoutManager = LinearLayoutManager(context)

        val nutritions = nutritionViewModel.nutritions
        val adapter = NutritionListAdapter(nutritions){nutritionId ->
            findNavController().navigate(
                NutritionListFragmentDirections.showNutritionDetail(nutritionId)
            )
        }
        binding.nutritionRecyclerView.adapter = adapter

        val newNutritionId = UUID.randomUUID()
        binding.newDayButton.setOnClickListener {
            findNavController().navigate(NutritionListFragmentDirections.showNutritionDetail(newNutritionId))
        }

        return binding.root
    }
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