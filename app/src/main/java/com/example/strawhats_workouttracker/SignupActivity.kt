package com.example.strawhats_workouttracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.strawhats_workouttracker.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "SignupActivity"
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var loginText: TextView
    private lateinit var signupButton: Button
    private lateinit var usernameEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_signup)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        usernameEditText = findViewById(R.id.usernameSignupEditText)
        passwordEditText = findViewById(R.id.passwordSignupEditText)

        signupButton = findViewById(R.id.signUpButton)
        signupButton.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()){
                signupUser(username, password)
            } else {
                Log.d(TAG, "Not all fields were filled out")
            }
        }

        loginText = findViewById(R.id.loginRedirect)
        loginText.setOnClickListener{
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun signupUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val id = databaseReference.push().key
                    val userData = UserData(id, username, password)
                    databaseReference.child(id!!).setValue(userData)
                    Log.d(TAG, "Signup successful")
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Log.d(TAG, "User already exists")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Database error: ${error.details}")
            }
        })
    }
}