package com.example.smarternships.data.model

import android.view.Menu
import com.example.smarternships.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class DataBase {
    companion object {
        public fun setUserName(id: String, name: String) {
            var users = FirebaseDatabase.getInstance().getReference("users")
            users.child(id).child("username").setValue(name)
        }
    }


}