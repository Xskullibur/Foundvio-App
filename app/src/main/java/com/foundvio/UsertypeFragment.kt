package com.foundvio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.foundvio.databinding.FragmentUsertypeBinding

class UsertypeFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentUsertypeBinding.inflate(inflater)

        // Caregiver Button
        binding.trackerBtn.setOnClickListener {
            // TODO: Caregiver Setup (Navigate to Register)
            navController.navigate(R.id.action_usertypeFragment_to_addTrackeeFragment)
        }

        // Elderly Button
        binding.trackeeBtn.setOnClickListener {
            // TODO: Elderly Setup
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}