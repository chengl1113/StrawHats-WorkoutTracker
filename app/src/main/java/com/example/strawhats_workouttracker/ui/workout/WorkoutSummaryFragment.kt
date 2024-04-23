package com.example.strawhats_workouttracker.ui.workout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentWorkoutSummaryBinding

private const val TAG = "WorkoutSummaryFragment"
class WorkoutSummaryFragment : Fragment() {

    private var _binding: FragmentWorkoutSummaryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutSummaryBinding.inflate(inflater, container, false)

        val args: WorkoutSummaryFragmentArgs by navArgs()
        val workout = args.workout
        Log.d(TAG, "workout: $workout")

        // set date text
        binding.dateTextview.text = "Date completed: ${workout.date}"

        // set duration text
        var seconds = workout.duration
        val hours = seconds / 3600
        seconds %= 3600
        val minutes = seconds / 60
        seconds %= 60

        binding.durationTextview.text = "Duration: " + String.format("%02d:%02d:%02d", hours, minutes, seconds)

        // create exercise item view
        for (exercise in workout.exercises) {
            val newExercise = layoutInflater.inflate(R.layout.list_item_exercise, null)
            val exerciseTitleView = newExercise.findViewById<TextView>(R.id.exercise_title)
            exerciseTitleView.text = exercise.name
            val setContainer = newExercise.findViewById<LinearLayout>(R.id.setInfoContainer)

            // create set item view
            for (set in exercise.sets) {
                val newSet = layoutInflater.inflate(R.layout.list_item_set, null)
                val setDetail = newSet.findViewById<TextView>(R.id.set_detail)
                setDetail.text = "${set.weight} X ${set.reps} @ RPE ${set.rpe}"

                setContainer.addView(newSet)
            }

            binding.exerciseContainer.addView(newExercise)
        }

        return binding.root
    }
}