package com.example.strawhats_workouttracker.ui.workout

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.databinding.ListItemWorkoutBinding

class WorkoutHolder(
    private val binding: ListItemWorkoutBinding
) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(workout: Workout, onWorkoutClicked: (Workout) -> Unit) {
        binding.workoutDateTextview.text = workout.date.toString()
        var seconds = workout.duration
        val hours = seconds / 3600
        seconds %= 3600
        val minutes = seconds / 60
        seconds %= 60
        binding.durationChip.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        binding.root.setOnClickListener { onWorkoutClicked(workout)}
    }
}

class WorkoutAdapter(
    private val workouts: List<Workout>,
    private val onWorkoutClicked: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkoutBinding.inflate(inflater, parent, false)
        return WorkoutHolder(binding)
    }

    override fun getItemCount(): Int { return workouts.size }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {
        val workout = workouts[position]
        holder.bind(workout) { onWorkoutClicked(workout) }
    }

}