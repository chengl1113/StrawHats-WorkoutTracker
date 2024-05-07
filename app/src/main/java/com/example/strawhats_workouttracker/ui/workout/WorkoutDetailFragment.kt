package com.example.strawhats_workouttracker.ui.workout

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentWorkoutDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

private const val TAG = "WorkoutDetailFragment"
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

    // store workout object to add to database
    private lateinit var workout: Workout

    // references to database
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    // use shared prefs to get userId
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId : String

    // viewmodel reference
    private val workoutViewModel: WorkoutViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "").toString()
        exercises = mutableMapOf<String, View>()

        // updating views and stuff

        binding.newExerciseButton.setOnClickListener{
           addNewExercise()
        }

        binding.newSetButton.setOnClickListener{
            addNewSet()
        }

        binding.endWorkoutButton.setOnClickListener{
            stopTimer()
            workout.duration = seconds
            Log.d(TAG, "workout: $workout")
            // add to database
            saveWorkoutToDatabase(workout)
        }

        // create workout object
        workout = Workout(date = LocalDate.now(), duration = 0, exercises = mutableListOf())

        // do database stuff
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users").child(userId).child("workouts")

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

        // update existing card view
        val newSet = layoutInflater.inflate(R.layout.list_item_set, null)
        val weight = binding.weightNumberText.text.toString()
        val reps = binding.repsNumberText.text.toString()
        val rpe = binding.rpeSlider.value.toString()
        if (exerciseName == "" || weight == "" || reps == "" || rpe == "") {
            view?.let {
                Snackbar.make(it, R.string.exercise_error, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.light_blue))
                    .setTextColor(resources.getColor(R.color.off_white))
                    .show()
            }
            return
        }

        if (exerciseName !in exercises.keys) {
            // add a new exercise
            addNewExercise()
        }
        val text = "$weight X $reps @ RPE $rpe"
        val setInfoTextView = newSet.findViewById<TextView>(R.id.set_detail)
        setInfoTextView.text = text
        setInfoTextView.setTextColor(resources.getColor(R.color.off_white))

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

        // add to workout object
        workout.exercises.find{ exercise -> exercise.name == exerciseName }?.sets?.plusAssign(Set(weight.toInt(), reps.toInt(), rpe.toFloat()))

    }

    private fun updateSetCounter() {
        val setNumberTextView = binding.setNumber
        val currentSetNumber = setNumberTextView.text.toString().toInt()
        val newSetNumber = currentSetNumber + 1
        setNumberTextView.text = newSetNumber.toString()
    }

    private fun addNewExercise() {
        val exerciseName = binding.exerciseNameText.text.toString()
        if (exerciseName == "") {
            view?.let {
                Snackbar.make(it, R.string.exercise_error, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.light_blue))
                    .setTextColor(resources.getColor(R.color.off_white))
                    .show()
            }
            return
        }
        val newExercise = layoutInflater.inflate(R.layout.list_item_exercise, null)
        val exerciseTitleView = newExercise.findViewById<TextView>(R.id.exercise_title)
        exerciseTitleView.text = exerciseName

        exercises[exerciseTitleView.text.toString()] = newExercise
        binding.exerciseContainer.addView(newExercise)

        //reset set number text
        binding.setNumber.text = "1"

        // add exercise to workout object
        workout.exercises += Exercise(exerciseName, sets = mutableListOf())

    }

    private fun stopTimer() {
        running = false
        handler.removeCallbacks(runnable)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveWorkoutToDatabase(workout: Workout) {
        val newWorkout = databaseReference.push()
        newWorkout.setValue(workout)
        workoutViewModel.addWorkout(workout)
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}