package com.example.smarternships.data.model

import com.google.firebase.database.DataSnapshot


interface OnGetDataListener {
    fun onSuccess(dataSnapshot: DataSnapshot?)
    fun onStart()
    fun onFailure()
}