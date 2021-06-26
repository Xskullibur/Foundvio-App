package com.foundvio.module

import android.util.Log
import com.foundvio.model.ObjectTypeInfoHelper
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.hmf.tasks.Task
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

const val TAG = "FoundvioAGCloudDBModule"

@Module
@InstallIn(SingletonComponent::class)
object FoundvioAGCloudDBModule {

    @Provides
    fun provideAGCloudDB(): Task<CloudDBZone> {
        val mCloudDB = AGConnectCloudDB.getInstance()
        mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())
        val mConfig = CloudDBZoneConfig("Foundvio",
            CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC)
        mConfig.persistenceEnabled = true
        val task = mCloudDB.openCloudDBZone2(mConfig, true)
        task.addOnSuccessListener {
            Log.i(TAG, "Open cloudDBZone success")
        }.addOnFailureListener {
            Log.w(TAG, "Open cloudDBZone failed for " + it.message)
        }
        return task

    }

}