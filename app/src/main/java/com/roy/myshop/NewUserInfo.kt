package com.roy.myshop

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.roy.myshop.databinding.ActivityNewUserInfoBinding
import com.roy.myshop.databinding.ActivitySignUpBinding

class NewUserInfo : AppCompatActivity() {
    private lateinit var binding: ActivityNewUserInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new_user_info)
        binding = ActivityNewUserInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.submit.setOnClickListener {
            val sNickname = binding.nickName.text.toString()
            setNickname(sNickname)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}