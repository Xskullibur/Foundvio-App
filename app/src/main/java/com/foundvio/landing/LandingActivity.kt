package com.foundvio.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.foundvio.R
import com.foundvio.databinding.ActivityLandingBinding
import com.foundvio.service.UserService
import com.foundvio.setup.AddTrackeeFragment
import com.foundvio.utils.isSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val LAST_FRAGMENT_ID = "LAST_FRAGMENT_ID"

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    private var currentFragment: Fragment = HomeFragment()
    private var currentFragmentId = HomeFragment.ID

    private fun switchFragment(fragment: Fragment){
        this.currentFragment = fragment

        currentFragmentId = when(currentFragment){
            is HomeFragment -> HomeFragment.ID
            is SocialFragment -> SocialFragment.ID
            else -> -1
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
    }

    //This is to forward the onActivityResult to the Fragment so the Fragment can receive the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentFragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            bottomNavigation.setOnItemSelectedListener{ item ->
                when(item.itemId){
                    R.id.home_menu -> {
                        switchFragment(HomeFragment())
                        true
                    }
                    R.id.social_menu -> {
                        switchFragment(SocialFragment())
                        true
                    }
                    R.id.settings_menu -> {
                        switchFragment(AddTrackeeFragment())
                        true
                    }
                    else -> false
                }
            }
        }

        if (savedInstanceState == null) {
            switchFragment(HomeFragment())
        }else{
            currentFragmentId = savedInstanceState.getInt(LAST_FRAGMENT_ID)
            when(currentFragmentId){
                HomeFragment.ID -> switchFragment(HomeFragment())
                SocialFragment.ID -> switchFragment(SocialFragment())
                else -> -1
            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(LAST_FRAGMENT_ID, currentFragmentId)
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