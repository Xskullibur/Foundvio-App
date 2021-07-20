package com.foundvio

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.foundvio.databinding.FragmentTrackeeQrBinding
import com.foundvio.service.UserService
import com.foundvio.utils.isSuccess
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.hmsscankit.WriterException
import com.huawei.hms.ml.scan.HmsBuildBitmapOption
import com.huawei.hms.ml.scan.HmsScan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Paths
import javax.inject.Inject

@AndroidEntryPoint
class TrackeeQrFragment : Fragment() {

    @Inject
    lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = FragmentTrackeeQrBinding.inflate(inflater)

        // Generate QR Code
        val qrOptions = HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.TRANSPARENT)
            .setBitmapColor(Color.rgb(0, 135, 135)).setBitmapMargin(1).create()
        try {

            lifecycleScope.launch(Dispatchers.IO) {
                val response = userService.userDetails()
                if (response.isSuccess()) {

                    val user = response.body()!!.message

                    // Generate QR Bitmap
                    val qrBitMap = ScanUtil.buildBitmap(
                        user.id.toString(),
                        HmsScan.QRCODE_SCAN_TYPE,
                        720,
                        720,
                        qrOptions
                    )
                    withContext(Dispatchers.Main) { binding.qrImageview.setImageBitmap(qrBitMap) }

                    // Create File for BitMap
                    val imagesDir = File(requireContext().cacheDir, "images")
                    // Create 'images' folder if not exists
                    if(!imagesDir.exists()){imagesDir.mkdirs()}
                    val file = File.createTempFile("foundvio", ".png", imagesDir)

                    // Convert Bitmap into ByteArray
                    qrBitMap.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())

                    // Get Uri from File using FileProvider
                    val bitmapUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().applicationContext.packageName + ".provider",
                        file
                    )

                    // Update AccountId
                    withContext(Dispatchers.Main) {
                        binding.accountId.text = user.id.toString()
                        // Set onClickListener
                        binding.materialCardView.setOnClickListener {

                            // Create Intent
                            val sharingIntent = Intent.createChooser(Intent().apply {
                                action = Intent.ACTION_SEND_MULTIPLE
                                putExtra(
                                    Intent.EXTRA_TEXT, "Hey, add me as your " +
                                            "care-receiver at ${user.id}"
                                )

                                data = bitmapUri
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                            }, null)

                            // Present ShareSheet
                            startActivity(sharingIntent)
                        }
                    }

                }
            }

        } catch (e: WriterException) {
            Log.w("buildBitmap", e)
        }

        return binding.root
    }
}