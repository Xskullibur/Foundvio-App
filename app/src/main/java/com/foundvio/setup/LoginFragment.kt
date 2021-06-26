package com.foundvio.setup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.foundvio.databinding.FragmentLoginBinding
import com.foundvio.landing.LandingActivity
import com.foundvio.model.User
import com.foundvio.service.UserService
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var navController: NavController
    private lateinit var launcher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var userService: UserService

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

                    userService.addUser(User().apply {
                        email = authAccount.email
                        name = authAccount.givenName
//                        huaweiToken = authAccount.idToken
                    })

                    val intent = Intent(this@LoginFragment.requireContext(), LandingActivity::class.java)
                    startActivity(intent)

                } else {
                    // The sign-in failed. No processing is required. Logs are recorded for fault locating.
                    Log.e(TAG, "sign in failed : " + (authAccountTask.exception as ApiException).statusCode)
                }
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

                val authParams  = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                    .setIdToken()
                    .setAccessToken()
                    .createParams()
                val service = AccountAuthManager.getService(this@LoginFragment.requireContext(), authParams)
                launcher.launch(service.signInIntent)
            }
        }
        return binding.root
    }
}