package com.foundvio.setup

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundvio.R
import com.foundvio.databinding.FragmentAddTrackeeBinding
import com.foundvio.landing.LandingActivity
import com.foundvio.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions


class AddTrackeeFragment : Fragment() {

    private lateinit var adapter: TrackeeAdapter
    private val viewModel: SetupViewModel by activityViewModels()

    companion object {
        private const val REQUEST_CODE_SCAN_ONE = 0X01
    }

    // Start Scanning for Result
    private fun startQrScan() {
        ScanUtil.startScan(activity, REQUEST_CODE_SCAN_ONE, HmsScanAnalyzerOptions.Creator()
            .setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create())
    }

    // Permission Callback
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {

            // Scan for Input
            startQrScan()
        }
        else {

            // Manual Input
            val inputDialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.input_dialog_view, null)

            MaterialAlertDialogBuilder(requireContext()).setView(inputDialogView)
                .setTitle("Add Elderly/Child")
                .setPositiveButton("Add") { dialog, _ ->

                    val userId = inputDialogView.findViewById<TextInputLayout>(R.id.userId_txt)
                        .editText!!.text.toString()
                    if (userId.isNotBlank()) {

                        // TODO: Get User from phone or userId
                        val user = User()
                        user.phone = userId
                        viewModel.addTrackee(user)
                    }

                    dialog.dismiss()
                }
                .show()
        }
    }

    // QR Scanner Result Callback
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK || data == null) {
            return
        }
        //Default View
        if (requestCode == REQUEST_CODE_SCAN_ONE) {

            // Input an image for scanning and return the result.
            val obj = data.getParcelableExtra(ScanUtil.RESULT) as HmsScan?
            if (obj != null) {

                // Log Results
                Log.i("Scan Result", obj.showResult)

                // TODO: Get User with Phone or UserId
                val user = User()
                user.phone = obj.showResult

                // Add Trackee to Tracker
                viewModel.addTrackee(user)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddTrackeeBinding.inflate(inflater)

        viewModel.toast.observe(viewLifecycleOwner) {
            if(it != null)Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.apply {

            // Add Trackee
            addTrackeeBtn.setOnClickListener {
                when {
                    // Check for permissions
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {

                        // Permission Granted (Start Scan)
                        startQrScan()
                    }

                    // Define Permission Rationale
                    shouldShowRequestPermissionRationale("Camera") -> {

                        // TODO: Show In Context UI
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected. In this UI,
                        // include a "cancel" or "no thanks" button that allows the user to
                        // continue using your app without granting the permission.
                    }

                    // Request for Permission
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }

            // Complete Adding
            doneBtn.setOnClickListener {
                viewModel.trackees.value?.let {
                    if (it.size <= 0) {
                        Toast.makeText(
                            this@AddTrackeeFragment.context,
                            "Please add a Trackee by click on the plus button", Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        //Register the user after he/she is done with adding the trackees
                        viewModel.registerUser()
                    }
                }

            }

            trackeeRecyclerView.layoutManager = LinearLayoutManager(this@AddTrackeeFragment.context)
            viewModel.trackees.value?.let {
                adapter = TrackeeAdapter(binding, it)
                trackeeRecyclerView.adapter = adapter
                val itemTouchHelper = ItemTouchHelper(
                    TrackeeAdapter.SwipeToDeleteCallback(
                        this@AddTrackeeFragment.requireContext(),
                        adapter
                    )
                )
                itemTouchHelper.attachToRecyclerView(trackeeRecyclerView)
            }

            viewModel.trackees.observe(viewLifecycleOwner) {
                adapter.notifyItemRangeChanged(it.size - 1, it.size)
            }
        }

        return binding.root
    }

}