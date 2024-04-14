package com.example.strawhats_workouttracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.strawhats_workouttracker.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var signupText: TextView
    private lateinit var loginButton: Button
    private lateinit var usernameEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        // if the user has been logged in, go to main activity
        sharedPreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("logged in?", false)) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }


        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)


        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        usernameEditText = findViewById(R.id.usernameLoginEditText)
        passwordEditText = findViewById(R.id.passwordLoginEditText)

        loginButton = findViewById(R.id.loginUpButton)
        loginButton.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()){
                loginUser(username, password)
            } else {
                Log.d(TAG, "Not all fields were filled out")
            }
        }

        signupText = findViewById(R.id.signupRedirect)
        signupText.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }
    }

    private fun loginUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password) {
                            Log.d(TAG, "Login Successful")
                            // save username and the fact that the user is logged in
                            val editor = sharedPreferences.edit()
                            editor.putString("username", username)
                            editor.putBoolean("logged in?", true)
                            editor.apply()

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                            return
                        }
                    }
                }
                Log.d(TAG, "Login failed")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Database error: ${error.message}")
            }
        })
    }
}