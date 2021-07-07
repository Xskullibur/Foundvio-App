package com.foundvio.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.foundvio.R
import com.foundvio.databinding.FragmentRegisterDetailsBinding

class RegisterDetailsFragment : Fragment() {

    private lateinit var navController: NavController

    private val viewModel: SetupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegisterDetailsBinding.inflate(inflater, container, false)

        binding.apply {
            binding.details = viewModel.setupUserDetails


            // Show Validations Message
            viewModel.setupUserDetails.phoneValidation.observe(viewLifecycleOwner){
                phoneTextLayout.error = it
            }
            viewModel.setupUserDetails.familyNameValidation.observe(viewLifecycleOwner){
                familyNameTextLayout.error = it
            }
            viewModel.setupUserDetails.givenNameValidation.observe(viewLifecycleOwner){
                givenNameTextLayout.error = it
            }

            readyButton.setOnClickListener {
                if(viewModel.setupUserDetails.validate()){
                    navController.navigate(R.id.action_registerDetailsFragment_to_usertypeFragment)
                }
            }
        }

        return binding.root
    }
}