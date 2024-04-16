package com.example.strawhats_workouttracker.ui.workout

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate

private const val TAG = "WorkoutRepository"
class WorkoutRepository(userId: String) {

    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference: DatabaseReference = firebaseDatabase.getReference("users").child(userId).child("workouts")

    fun getWorkouts(callback: (MutableList<Workout>) -> Unit){
        val workouts = mutableListOf<Workout>()

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                // loop through each workout
                for (workoutSnapshot in snapshot.children) {
//                    Log.d(TAG, "workoutSnapshot: $workoutSnapshot")
                    val dayOfMonth = workoutSnapshot.child("date").child("dayOfMonth").getValue(Int::class.java)
                    val month = workoutSnapshot.child("date").child("monthValue").getValue(Int::class.java)
                    val year = workoutSnapshot.child("date").child("year").getValue(Int::class.java)

                    val workoutDate = LocalDate.of(year!!, month!!, dayOfMonth!!)
                    val workoutDuration = workoutSnapshot.child("duration").getValue(Int::class.java)
                    val exercises = mutableListOf<Exercise>()

                    val exercisesRef = workoutSnapshot.child("exercises")

                    // loop through each exercise
                    for (exerciseSnapshot in exercisesRef.children) {
                        // get exercise data
                        Log.d(TAG, "exerciseSnapshot: $exerciseSnapshot")
                        val exerciseName = exerciseSnapshot.child("name").getValue(String::class.java)
                        val exerciseSets = exerciseSnapshot.child("sets")

                        val sets = mutableListOf<Set>()
                        // loop through each set

                        for (setSnapshot in exerciseSets.children) {
                            Log.d(TAG, "setSnapshot: $setSnapshot")
                            val weight = setSnapshot.child("weight").getValue(Int::class.java)
                            val reps = setSnapshot.child("reps").getValue(Int::class.java)
                            val rpe = setSnapshot.child("rpe").getValue(Float::class.java)

                            sets += Set(weight!!, reps!!, rpe!!)
                        }

                        Log.d(TAG, "sets: $sets")

                        exercises += Exercise(exerciseName!!, sets)
                    }

                    workouts += Workout(workoutDate!!, workoutDuration!!, exercises)
                }

                callback(workouts)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: DATABASE ERROR")
            }

        })

    }

}