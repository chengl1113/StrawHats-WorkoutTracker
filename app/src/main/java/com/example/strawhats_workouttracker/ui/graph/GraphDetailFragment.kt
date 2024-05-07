package com.example.strawhats_workouttracker.ui.graph

import DateAxisValueFormatter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import com.example.strawhats_workouttracker.R
import com.example.strawhats_workouttracker.databinding.FragmentGraphDetailBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.LocalDate
import java.time.ZoneOffset
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.strawhats_workouttracker.ui.workout.Set

private const val TAG = "GraphDetailFragment"
class GraphDetailFragment : Fragment() {
    private var _binding: FragmentGraphDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args: GraphDetailFragmentArgs by navArgs()

        _binding = FragmentGraphDetailBinding.inflate(inflater, container, false)

        binding.graphExerciseName.text = args.exerciseName

        var data: HashMap<LocalDate, MutableList<Set>>
        
        getData(args.exerciseName, args.userId, object: DataCallback {
            override fun onDataReceived(setsMap: HashMap<LocalDate, MutableList<Set>>) {
                data = setsMap
                Log.d(TAG, "data size: ${data.size}")
                createChart(data)
            }
        })


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChart(data: HashMap<LocalDate, MutableList<Set>>) {
        // Convert the entries to a list of pairs
        val sortedEntries = data.entries.sortedBy { it.key }

        // Convert the sorted list back to a map
        val sortedData = sortedEntries.associate { it.toPair() }

        val chart = binding.lineChart


        val dates = sortedData.keys.toList()

        // Convert LocalDates to Unix timestamps (in milliseconds)
        val timestamps = dates.map { it.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli().toFloat() }

        // Calculate average one rep max per day
        val vals = mutableListOf<Double>()
        for ((date, sets) in sortedData) {
            var total = 0.0
            for (set in sets) {
                val max = (set.weight * (1 + (set.reps / 30.0))) / (1.0278 - (0.0278 * set.rpe))
                total += max
            }
            val average = total / sets.size
            vals.add(average)
        }

        // Example list of data values
        val values = vals.toList()

        // Create entries
        val entries = timestamps.mapIndexed { index, timestamp ->
            Entry(timestamp, values[index].toFloat())
        }

        // Create a data set
        val dataSet = LineDataSet(entries, "Data")

        dataSet.color = resources.getColor(R.color.off_white)
        dataSet.valueTextSize = 16f

        // Set data to the chart
        chart.data = LineData(dataSet)
        chart.data.setValueTextColor(resources.getColor(R.color.off_white))

        // Format x-axis labels as dates
        chart.xAxis.valueFormatter = DateAxisValueFormatter()
        chart.xAxis.textColor = resources.getColor(R.color.off_white)
        chart.axisLeft.textColor = resources.getColor(R.color.off_white)
        chart.axisRight.textColor = resources.getColor(R.color.off_white)
        chart.legend.textColor = resources.getColor(R.color.off_white)
        chart.description.text = ""


        // Refresh chart
        chart.invalidate()
    }

    private fun getData(exerciseName: String, userId: String, callback: DataCallback){

        val database = FirebaseDatabase.getInstance()
        val workoutsRef = database.getReference("users/$userId/workouts")

        // Map to store sets with LocalDate as key
        val setsMap: HashMap<LocalDate, MutableList<Set>> = hashMapOf()

        // Query to retrieve all workouts
        workoutsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (workout in dataSnapshot.children) {
//                    Log.d(TAG, "workout: $workout")
                    // Get the date
                    val month = workout.child("date").child("monthValue").getValue(Int::class.java)
                    val day = workout.child("date").child("dayOfMonth").getValue(Int::class.java)
                    val year = workout.child("date").child("year").getValue(Int::class.java)
                    val date = LocalDate.of(year!!, month!!, day!!)


                    // loop through the exercises
                    for (exercise in workout.child("exercises").children) {
                        val name = exercise.child("name").getValue(String::class.java)

                        // If name is same as exercise name, loop through the sets
                        if (name == exerciseName) {
                            Log.d(TAG, "name: $name")
                            for (set in exercise.child("sets").children) {
                                val weight = set.child("weight").getValue(Int::class.java)
                                val reps = set.child("reps").getValue(Int::class.java)
                                val rpe = set.child("rpe").getValue(Float::class.java)
                                Log.d(TAG, "weight: $weight, reps: $reps, rpe: $rpe")
                                val curSet = Set(weight!!, reps!!, rpe!!)
                                Log.d(TAG, "set: $curSet")

                                // add to setsMap
                                if (date !in setsMap.keys) {
                                    setsMap[date] = mutableListOf()
                                }
                                setsMap[date]?.add(curSet)
                            }
                        }
                    }
                }
                callback.onDataReceived(setsMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error retrieving data: ${databaseError.message}")
            }
        })
    }
}