package com.foundvio.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.foundvio.R
import com.foundvio.databinding.FragmentLoginBinding
import com.foundvio.landing.LandingActivity
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.agconnect.core.service.auth.TokenSnapshot
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var navController: NavController
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
                if (authAccountTask.isSuccessful) {
                    // The sign-in is successful, and the user's ID information and ID token are obtained.
                    val authAccount = authAccountTask.result
                    Log.i(TAG, "idToken:" + authAccount.idToken)
                    processLogin(authAccount)
                } else {
                    // The sign-in failed. No processing is required. Logs are recorded for fault locating.
                    Log.e(TAG, "sign in failed : " + (authAccountTask.exception as ApiException).statusCode)
                    Toast.makeText(this@LoginFragment.context,
                        "Error signing in", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    /**
     * Handle the login process after Huawei ID Login Activity Succeed
     *
     * If the user is logging in, this will navigate to [LandingActivity]
     *
     * If the user is registering a new account with the Huawei ID, this will navigate to
     * [UsertypeFragment]
     *
     */
    private fun processLogin(
        authAccount: AuthAccount
    ) {
        val accessToken = authAccount.accessToken
        if (AGConnectAuth.getInstance().currentUser == null) {
            val credential = HwIdAuthProvider.credentialWithToken(accessToken)
            AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
                viewModel.retrieveAccessToken()
                // onSuccess

                if(viewModel.isRegister){
                    //Jump to register fragment if the user is registering
                    navController.navigate(R.id.action_loginFragment_to_usertypeFragment)
                }else{
                    //Jump the landing activity if the user is logging in
                    startLandingActivity()
                }


            }.addOnFailureListener {
                // onFail
                Toast.makeText(
                    this@LoginFragment.context,
                    "Error signing in", Toast.LENGTH_LONG
                )
                    .show()
            }
        } else {
            viewModel.retrieveAccessToken()
            if(viewModel.isRegister){
                //Jump to register fragment if the user is registering
                navController.navigate(R.id.action_loginFragment_to_usertypeFragment)
            }else{
                //Jump the landing activity if the user is logging in
                startLandingActivity()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.apply {
            loginButton.setOnClickListener {
                viewModel.isRegister = false
                startHuaweiLoginActivity()
            }
            registerButton.setOnClickListener {
                viewModel.isRegister = true
                startHuaweiLoginActivity()
            }
        }
        return binding.root
    }

    /**
     * Start the [LandingActivity] Activity
     */
    private fun startLandingActivity(){
        val intent = Intent(this@LoginFragment.context, LandingActivity::class.java)
        startActivity(intent)
    }

    /**
     * Start the Huawei ID Login Activity
     */
    private fun startHuaweiLoginActivity() {
        val authParams = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .setAccessToken()
            .setUid()
            .setMobileNumber()
            .setEmail()
            .createParams()
        val service = AccountAuthManager.getService(this@LoginFragment.requireContext(), authParams)
        launcher.launch(service.signInIntent)
    }
}