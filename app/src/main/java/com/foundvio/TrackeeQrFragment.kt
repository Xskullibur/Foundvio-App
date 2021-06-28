package com.foundvio

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foundvio.databinding.FragmentTrackeeQrBinding
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.hmsscankit.WriterException
import com.huawei.hms.ml.scan.HmsBuildBitmapOption
import com.huawei.hms.ml.scan.HmsScan

class TrackeeQrFragment : Fragment() {

    companion object {
        const val ID = "TrackeeID123"
    }

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
        val qrOptions = HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.TRANSPARENT).setBitmapColor(Color.rgb(0, 135, 135)).setBitmapMargin(1).create()
        try {
            val qrBitMap = ScanUtil.buildBitmap(ID, HmsScan.QRCODE_SCAN_TYPE, 720, 720, qrOptions)
            binding.qrImageview.setImageBitmap(qrBitMap)
        }
        catch (e: WriterException) {
            Log.w("buildBitmap", e)
        }

        return binding.root
    }
}