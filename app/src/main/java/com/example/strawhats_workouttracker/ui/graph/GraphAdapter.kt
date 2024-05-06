package com.example.strawhats_workouttracker.ui.graph

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.ListItemExerciseBinding
import com.example.strawhats_workouttracker.ui.workout.Exercise

class GraphHolder(
    private val binding: ListItemExerciseBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(exercise: Exercise, onItemClicked: (exerciseName : String) -> Unit) {
        binding.exerciseTitle.text = exercise.name
        binding.root.setOnClickListener{onItemClicked(exercise.name)}
    }
}

class GraphAdapter(
    private val exercises: List<Exercise>,
    private val onItemClicked: (exerciseName: String) -> Unit
) : RecyclerView.Adapter<GraphHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemExerciseBinding.inflate(inflater, parent, false)

        return GraphHolder(binding)
    }

    override fun getItemCount(): Int { return exercises.size }

    override fun onBindViewHolder(holder: GraphHolder, position: Int) {
        val exercise = exercises[position]
        val exerciseName = exercise.name
        holder.bind(exercise) { onItemClicked(exerciseName) }

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation))
    }

}