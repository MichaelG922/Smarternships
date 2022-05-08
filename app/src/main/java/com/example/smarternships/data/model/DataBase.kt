package com.example.smarternships.data.model

import android.util.Log
import com.google.firebase.database.*


class DataBase {
    companion object {
        fun setUser(id: String, user: User) {
            var users = FirebaseDatabase.getInstance().getReference("users")
            users.child(id).setValue(user)
        }

        fun setJob(id: String, job: Job) {
            var jobs = FirebaseDatabase.getInstance().getReference("jobs")
            jobs.child(id).setValue(job)
        }

        fun deleteJob(jobID: String) {
            var jobs = FirebaseDatabase.getInstance().getReference("jobs")
            jobs.child(jobID).removeValue()
        }

        fun addJobToUser(jobId: String, userId: String) {
            getUser(userId, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var user = dataSnapshot?.getValue(User::class.java)!!
                    user.jobs = user.jobs.plus(jobId).distinct()
                    setUser(userId, user)
                }
                override fun onStart() {}
                override fun onFailure() {}
            })
        }

        fun removeJobFromUser(jobId: String, userId: String) {
//            var users = FirebaseDatabase.getInstance().getReference("users")
//            var user = users.child(userId)
//            var jobs = FirebaseDatabase.getInstance().getReference("jobs")
//            var job = jobs.child(jobId)
//
//            if(user){
//
//            }


            getUser(userId, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {

                    var user = dataSnapshot?.getValue(User::class.java)!!
                    if(user.jobs.indexOf(jobId) != -1) {
                        user.jobs = user.jobs.toMutableList().apply {
                            removeAt(user.jobs.indexOf(jobId))
                        }
                        setUser(userId, user)
                        Log.i("USER-JOB Success", "Successfully to remove job from user")
                    }
                }
                override fun onStart() {}
                override fun onFailure() {
                    Log.i("USER-JOB ERR", "Failed to remove job from user")
                }
            })
            getJob(jobId, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var job = dataSnapshot?.getValue(Job::class.java)!!
                    job.assignedUserId =  ""
                    setJob(jobId, job)
                    Log.i("USER-JOB Success", "Successfully to remove user from job")

                }
                override fun onStart() {}
                override fun onFailure() {
                    Log.i("USER-JOB ERR", "Failed to remove user from job")
                }
            })
        }

        fun applyToJob(jobId: String, userId: String) {
            getJob(jobId, object : OnGetDataListener {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    var job = dataSnapshot?.getValue(Job::class.java)!!
                    job.applicants = job.applicants.plus(userId).distinct()
                    setJob(jobId, job)
                }
                override fun onStart() {}
                override fun onFailure() {}
            })
        }

        fun getUser(id: String, listener: OnGetDataListener) {
            var users = FirebaseDatabase.getInstance().getReference("users")
            var user = users.child(id)

            listener.onStart()
            user.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listener.onSuccess(dataSnapshot);
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onFailure()
                }
            })
        }

        fun getJob(id: String, listener: OnGetDataListener) {
            var jobs = FirebaseDatabase.getInstance().getReference("jobs")
            var job = jobs.child(id)

            listener.onStart()
            job.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listener.onSuccess(dataSnapshot);
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onFailure()
                }
            })
        }

        fun getAllJobs(listener: OnGetDataListener) {
            var jobs = FirebaseDatabase.getInstance().getReference("jobs")

            listener.onStart()
            jobs.addValueEventListener(object : ValueEventListener {
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