package com.foundvio.service

import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.hmf.tasks.Task
import javax.inject.Inject

class DatabaseService(
    var cloudDBZoneTask: Task<CloudDBZone>
) {

    lateinit var cloudDBZone: CloudDBZone

    /**
     * Initialise the CloudDB
     */
    fun init(){
        cloudDBZoneTask.addOnSuccessListener {
            cloudDBZone = it
        }
    }


}