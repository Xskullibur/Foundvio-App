package com.foundvio.setup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.foundvio.R
import com.foundvio.databinding.FragmentUsertypeBinding

class UsertypeFragment : Fragment() {

    private lateinit var navController: NavController
    private val viewModel: SetupViewModel by activityViewModels()

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
            viewModel.isTrackee = false
            navController.navigate(R.id.action_usertypeFragment_to_addTrackeeFragment)
        }

        // Elderly Button
        binding.trackeeBtn.setOnClickListener {
            // TODO: Elderly Setup
            viewModel.isTrackee = true
            navController.navigate(R.id.action_usertypeFragment_to_trackeeQrFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}