package com.example.strawhats_workouttracker.ui.graph

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.databinding.ListItemExerciseBinding
import com.example.strawhats_workouttracker.ui.workout.Exercise

class GraphHolder(
    private val binding: ListItemExerciseBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(exercise: Exercise) {
        binding.exerciseTitle.text = exercise.name
    }
}

class GraphAdapter(
    private val exercises: List<Exercise>
) : RecyclerView.Adapter<GraphHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemExerciseBinding.inflate(inflater, parent, false)

        return GraphHolder(binding)
    }

    override fun getItemCount(): Int { return exercises.size }

    override fun onBindViewHolder(holder: GraphHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

}