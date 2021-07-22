package com.foundvio.tracking

import android.app.PendingIntent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class TrackingResolutionActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pI = intent.getParcelableExtra<Parcelable>("resolution") as PendingIntent?
        val launcher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            println()
            finish()
        }
        launcher.launch(IntentSenderRequest.Builder(pI!!.intentSender).build())
    }


}