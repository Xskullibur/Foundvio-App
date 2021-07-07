package com.foundvio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foundvio.databinding.ActivityMainBinding
import com.foundvio.service.DatabaseService
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

    //This is to forward the onActivityResult to the Fragment so the Fragment can receive the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentByTag("CURRENT_FRAGMENT")
        fragment?.let {
            val fragment = it.childFragmentManager.fragments[0]
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


}