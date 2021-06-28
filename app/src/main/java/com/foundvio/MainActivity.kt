package com.foundvio

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foundvio.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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