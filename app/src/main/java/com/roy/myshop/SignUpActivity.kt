package com.roy.myshop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

import com.roy.myshop.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val TAG: String = "RoyTest"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
//        setContentView(R.layout.activity_sign_up)
        setContentView(view)

        binding.signup.setOnClickListener {
            val sEmail: String = binding.email.text.toString()
            val sPassword: String = binding.password.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful()) {
                        AlertDialog.Builder(this)
                            .setTitle("Sign Up")
                            .setMessage("Account created")
                            .setPositiveButton("OK") { dialog, which ->
                                setResult(Activity.RESULT_OK)
                                finish()
                            }.show()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("Sign Up")
                            .setMessage(it.exception?.message)
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
        }


    }

}