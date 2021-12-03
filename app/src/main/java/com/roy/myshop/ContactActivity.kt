package com.roy.myshop

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class ContactActivity : AppCompatActivity() {
    private val TAG = ContactActivity::class.java.simpleName
    private val RC_CONTACTS: Int = 110
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                RC_CONTACTS
            )
        } else {
            readContacts()
        }

//        readContacts()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts()
            }
        }
    }

    private fun readContacts() {
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                var name: String = ""
                val target = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                if (target > 0) name = cursor.getString(target)
                Log.d(TAG, "onCreate: $name ")
            }
        }
    }
}