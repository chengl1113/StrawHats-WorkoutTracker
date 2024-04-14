package com.example.strawhats_workouttracker.ui.workout

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentWorkoutDetailBinding

class WorkoutDetailFragment : Fragment(){

    private var _binding: FragmentWorkoutDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // for the timer
    private var running = false
    private var seconds = 0

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (running) {
                seconds++
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60
                val timeString = String.format("%02d:%02d:%02d", hours, minutes, secs)
                binding.timerTextView.text = timeString
                handler.postDelayed(this, 1000)
            }
        }
    }

    // to store the exercises being done
    private lateinit var exercises : MutableMap<String, View>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)

        exercises = mutableMapOf<String, View>()

        binding.newExerciseButton.setOnClickListener{
           addNewExercise()
        }

        binding.newSetButton.setOnClickListener{
            addNewSet()
        }

        binding.endWorkoutButton.setOnClickListener{
            stopTimer()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // start timer when the view is created
        running = true
        handler.post(runnable)
    }

    private fun addNewSet() {
        // update set counter
        updateSetCounter()
        // check if exercise has been added yet
        val exerciseName = binding.exerciseNameText.text.toString()
        if (exerciseName !in exercises.keys) {
            // add a new exercise
            addNewExercise()
        }
        // update existing card view
        val newSet = layoutInflater.inflate(R.layout.list_item_set, null)
        val weight = binding.weightNumberText.text.toString()
        val reps = binding.repsNumberText.text.toString()
        val rpe = binding.rpeSlider.value.toString()
        val text = "$weight X $reps @ RPE $rpe"
        val setInfoTextView = newSet.findViewById<TextView>(R.id.set_detail)
        setInfoTextView.text = text

        // get the view for the exercise
        exercises[exerciseName]?.findViewById<LinearLayout>(R.id.setInfoContainer)
            ?.addView(setInfoTextView)


        // clear text for reps, weight, and reset rpe slider
        binding.repsNumberText.text = SpannableStringBuilder("")
        binding.weightNumberText.text = SpannableStringBuilder("")
        binding.rpeSlider.value = 0F

        // hide keyboard after
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)

    }

    private fun updateSetCounter() {
        val setNumberTextView = binding.setNumber
        val currentSetNumber = setNumberTextView.text.toString().toInt()
        val newSetNumber = currentSetNumber + 1
        setNumberTextView.text = newSetNumber.toString()
    }

    private fun addNewExercise() {
        val exerciseName = binding.exerciseNameText.text.toString()
        val newExercise = layoutInflater.inflate(R.layout.list_item_exercise, null)
        val exerciseTitleView = newExercise.findViewById<TextView>(R.id.exercise_title)
        exerciseTitleView.text = exerciseName

        exercises[exerciseTitleView.text.toString()] = newExercise
        binding.exerciseContainer.addView(newExercise)

        //reset set number text
        binding.setNumber.text = "1"

    }

    private fun stopTimer() {
        running = false
        handler.removeCallbacks(runnable)
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}