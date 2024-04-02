package com.example.strawhats_workouttracker.ui.workout

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentWorkoutBinding

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null

    private val workoutViewModel: WorkoutViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)

        binding.workoutRecyclerView.layoutManager = LinearLayoutManager(context)

        val workouts = workoutViewModel.workouts
        val adapter = WorkoutAdapter(workouts) {
            findNavController().navigate(R.id.show_workout_detail)
        }
        binding.workoutRecyclerView.adapter = adapter

        binding.newWorkoutButton.setOnClickListener {
            findNavController().navigate(R.id.show_workout_detail)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}