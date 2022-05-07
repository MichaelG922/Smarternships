package com.example.smarternships.data.model

import android.R
import android.util.Log
import com.google.firebase.database.*


class DataBase {
    companion object {
        public fun setUser(id: String, user: User) {
            var users = FirebaseDatabase.getInstance().getReference("users")
            users.child(id).setValue(user)
        }

        public fun setJob(id: String, job: Job) {
            var jobs = FirebaseDatabase.getInstance().getReference("jobs")
            jobs.child(id).setValue(job)
        }

        public fun getUser(id: String, listener: OnGetDataListener) {
            var users = FirebaseDatabase.getInstance().getReference("users")
            var user = users.child(id)

            listener.onStart()
            user.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listener.onSuccess(dataSnapshot);
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onFailure()
                }
            })
        }

        public fun getJob(id: String, listener: OnGetDataListener) {
            var jobs = FirebaseDatabase.getInstance().getReference("jobs")
            var job = jobs.child(id)

            listener.onStart()
            job.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listener.onSuccess(dataSnapshot);
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onFailure()
                }
            })
        }

    }


}