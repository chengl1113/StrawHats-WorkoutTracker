package com.example.strawhats_workouttracker.ui.graph

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
import com.example.strawhats_workouttracker.databinding.FragmentGraphBinding
import com.example.strawhats_workouttracker.ui.workout.Exercise
import com.example.strawhats_workouttracker.ui.workout.Workout
import com.example.strawhats_workouttracker.ui.workout.WorkoutViewModel

private const val TAG = "GraphFragment"
class GraphFragment : Fragment() {

    // get userId
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId : String

    private var _binding: FragmentGraphBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val workoutViewModel : WorkoutViewModel by activityViewModels()

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

        _binding = FragmentGraphBinding.inflate(inflater, container, false)

        binding.graphRecylerView.layoutManager = LinearLayoutManager(context)


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel.userId = userId
        workoutViewModel.workouts.observe(viewLifecycleOwner) {workouts ->
            Log.d(TAG, "workouts: $workouts")
            val uniqueExercises = getUniqueExercises(workouts)
            val adapter = GraphAdapter(uniqueExercises) { exerciseName ->
                val action = GraphFragmentDirections.actionNavigationNotificationsToGraphDetailFragment(exerciseName, userId)
                findNavController().navigate(action)
            }
            binding.graphRecylerView.adapter = adapter
        }

    }

    private fun getUniqueExercises(workouts: List<Workout>) : List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        for (w in workouts) {
            for (e in w.exercises) {
                if (e !in exercises) {
                    exercises += e
                }
            }
        }
        return exercises
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
                findNavController().navigate(R.id.graph_to_accountFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}