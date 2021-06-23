package com.foundvio.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.foundvio.R
import com.foundvio.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            bottomNavigation.setOnItemSelectedListener{ item ->
                when(item.itemId){
                    R.id.manage_menu -> true
                    R.id.social_menu -> true
                    R.id.settings_menu -> true
                    else -> false
                }
            }
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}