package com.example.strawhats_workouttracker.ui.workout

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentWorkoutBinding

private const val TAG = "WorkoutFragment"
class WorkoutFragment : Fragment() {

    // get userId
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId : String

    private var _binding: FragmentWorkoutBinding? = null

    private val workoutViewModel: WorkoutViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "none").toString()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)

//        binding.workoutRecyclerView.layoutManager = LinearLayoutManager(context)
        // Set up the LinearLayoutManager with reverse layout
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true // Set reverseLayout to true
        layoutManager.stackFromEnd = true // Set stackFromEnd to true to start filling from the bottom (now top)
        binding.workoutRecyclerView.layoutManager = layoutManager

        binding.newWorkoutButton.setOnClickListener {
            findNavController().navigate(R.id.show_workout_detail)
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel.userId = userId
        workoutViewModel.workouts.observe(viewLifecycleOwner) {workouts ->
            Log.d(TAG, "workouts: $workouts")
            val adapter = WorkoutAdapter(workouts) { workout ->
                val action = WorkoutFragmentDirections.actionNavigationHomeToWorkoutSummaryFragment2(workout)
                findNavController().navigate(action)
            }
            binding.workoutRecyclerView.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_account -> {
                // Navigate to settings screen.
                findNavController().navigate(R.id.workout_to_accountFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}