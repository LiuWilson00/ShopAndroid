package com.roy.myshop

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ValueEventListener

val auth = FirebaseAuth.getInstance()
fun Activity.setNickname(nickname: String) {
    getSharedPreferences("shop", Context.MODE_PRIVATE).edit()
        .putString("NICKNAME", nickname)
        .apply()
    FirebaseDatabase.getInstance()
        .getReference("users")
        .child(auth.currentUser!!.uid)
        .child("nickname")
        .setValue(nickname)
}

fun Activity.getNickname(getNicknameHandler: (String) -> Any): String {
    val currentUser = auth.currentUser

    if (currentUser != null) {
        FirebaseDatabase.getInstance().getReference()
            .child("users")
            .child(currentUser.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    getNicknameHandler(snapshot.value as String)
                }

                override fun onCancelled(error: DatabaseError) {
                    getNicknameHandler(error.toString())
                }

            })
    }



    return getSharedPreferences("shop", Context.MODE_PRIVATE)
        .getString("NICKNAME", "")
        .toString()
}