package com.example.strawhats_workouttracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.strawhats_workouttracker.databinding.FragmentAccountBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "AccountFragment"
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar

        // Set the title of the ActionBar
        actionBar?.title = "Account Settings"

        sharedPreferences = requireContext().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)

        // load default values
        binding.changeUserNameEditText.text = Editable.Factory.getInstance().newEditable(sharedPreferences.getString("username", ""))
        binding.calorieGoalEditText.text = Editable.Factory.getInstance().newEditable(
            sharedPreferences.getInt("calorie goal", 0).toString()
        )

        // handle username change
        binding.changeUserNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    val editor = sharedPreferences.edit()
                    editor.putString("username", s.toString())
                    editor.apply()
                }
            }
        })

        // handle calorie goal being changed
        binding.calorieGoalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    val editor = sharedPreferences.edit()
                    editor.putInt("calorie goal", s.toString().toInt())
                    editor.apply()
                    view?.let {
                        Snackbar.make(it, R.string.goals_saved, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.light_blue))
                            .setTextColor(resources.getColor(R.color.off_white))
                            .show()
                    }
                }
            }
        })

        // handle log out button
        binding.logOutButton.setOnClickListener{
            logOut()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun logOut() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("logged in?", false)
        editor.apply()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

}