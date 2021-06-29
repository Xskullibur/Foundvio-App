package com.foundvio.module

import android.content.Context
import android.util.Log
import com.foundvio.model.ObjectTypeInfoHelper
import com.foundvio.service.DatabaseService
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.hmf.tasks.Task
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val TAG = "FoundvioAGCloudDBModule"

@Module
@InstallIn(SingletonComponent::class)
object FoundvioAGCloudDBModule {

    @Singleton
    @Provides
    fun provideAGCloudDB(@ApplicationContext context: Context): Task<CloudDBZone> {
        AGConnectCloudDB.initialize(context)
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

    @Singleton
    @Provides
    fun provideDatabaseService(cloudDBZoneTask: Task<CloudDBZone>): DatabaseService {
        return DatabaseService(cloudDBZoneTask)
    }

}