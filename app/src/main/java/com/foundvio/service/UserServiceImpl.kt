package com.foundvio.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.foundvio.model.User
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.hmf.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


const val TAG = "UserServiceImpl"

class UserServiceImpl @Inject constructor(
    @ApplicationContext var context: Context,
    var databaseService: DatabaseService
): UserService {

    override fun addUser(user: User) {
        val upsertTask = databaseService.cloudDBZone.executeUpsert(user)
        upsertTask.addOnSuccessListener { cloudDBZoneResult ->
            Log.i(TAG, "Upsert $cloudDBZoneResult records")
        }.addOnFailureListener {
            Toast.makeText(context, "Insert user failed", Toast.LENGTH_LONG).show()
            throw it
        }
    }
}