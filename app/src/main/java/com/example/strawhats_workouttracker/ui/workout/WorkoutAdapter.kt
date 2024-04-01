package com.example.strawhats_workouttracker.ui.workout

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.Workout
import com.example.strawhats_workouttracker.databinding.ListItemWorkoutBinding

class WorkoutHolder(
    val binding: ListItemWorkoutBinding
) : RecyclerView.ViewHolder(binding.root) {

}

class WorkoutAdapter(
    private val workouts: List<Workout>
) : RecyclerView.Adapter<WorkoutHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemWorkoutBinding.inflate(inflater, parent, false)
        return WorkoutHolder(binding)
    }

    override fun getItemCount(): Int { return workouts.size }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {
        val workout = workouts[position]
        holder.apply {
            binding.workoutDateTextview.text = workout.date.toString()
            var minutes = workout.duration.toMinutes()
            val hours = minutes / 60
            minutes -= (hours * 60)
            binding.durationChip.text = String.format("%02d:%02d", hours, minutes)
        }
    }

}