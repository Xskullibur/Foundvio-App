package com.foundvio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.foundvio.databinding.ActivityMainBinding
import com.foundvio.service.DatabaseService
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var databaseService: DatabaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseService.init()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}