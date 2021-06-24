package com.foundvio.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.foundvio.R
import com.foundvio.databinding.ActivityLandingBinding

const val LAST_FRAGMENT_ID = "LAST_FRAGMENT_ID"

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    private var currentFragment: Fragment = ManageFragment()
    private var currentFragmentId = ManageFragment.ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            bottomNavigation.setOnItemSelectedListener{ item ->
                when(item.itemId){
                    R.id.manage_menu -> {
                        switchFragment(ManageFragment())
                        true
                    }
                    R.id.social_menu -> {
                        switchFragment(SocialFragment())
                        true
                    }
                    R.id.settings_menu -> true
                    else -> false
                }
            }
        }

        if (savedInstanceState == null) {
            switchFragment(ManageFragment())
        }else{
            currentFragmentId = savedInstanceState.getInt(LAST_FRAGMENT_ID)
            when(currentFragmentId){
                ManageFragment.ID -> switchFragment(ManageFragment())
                SocialFragment.ID -> switchFragment(SocialFragment())
                else -> -1
            }
        }


    }

    private fun switchFragment(fragment: Fragment){
        this.currentFragment = fragment

        currentFragmentId = when(currentFragment){
            is ManageFragment -> ManageFragment.ID
            is SocialFragment -> SocialFragment.ID
            else -> -1
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(LAST_FRAGMENT_ID, currentFragmentId)
    }


}