package com.foundvio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.foundvio.databinding.ActivityMainBinding
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AGConnectCloudDB.initialize(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}