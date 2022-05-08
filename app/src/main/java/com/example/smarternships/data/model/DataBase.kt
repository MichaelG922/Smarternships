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

        public fun deleteJob(id: String) {
            var jobs = FirebaseDatabase.getInstance().getReference("jobs")
            jobs.child(id).removeValue()
        }

        public fun removeJobFromUser(jobId: String, userId: String) {
            getUser(userId, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var user = dataSnapshot?.getValue(User::class.java)!!
                    if (user != null) {
                        user.jobs = user.jobs.filter { key: String -> key != jobId }
                        DataBase.setUser(userId, user)
                    }
                }
                override fun onStart() {
                    //when starting
                    Log.d("ONSTART", "Started")
                }

                override fun onFailure() {
                    Log.d("onFailure", "Failed")
                }
            })
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