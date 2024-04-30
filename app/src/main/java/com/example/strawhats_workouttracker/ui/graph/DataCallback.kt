package com.example.strawhats_workouttracker.ui.graph

import java.time.LocalDate
import com.example.strawhats_workouttracker.ui.workout.Set


interface DataCallback {
    fun onDataReceived(setsMap: HashMap<LocalDate, MutableList<Set>>)
}