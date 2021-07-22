package com.foundvio.landing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.foundvio.R
import com.foundvio.databinding.FragmentHomeBinding
import com.foundvio.service.UserService
import com.foundvio.tracking.GeoCoord
import com.foundvio.tracking.TrackeeGeoCoordService
import com.foundvio.tracking.toLatLng
import com.foundvio.utils.hasPermissions
import com.foundvio.utils.isSuccess
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

class HomeFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val ID = 0
        private const val TAG = "HomeFragment"
    }

    private val viewModel by activityViewModels<HomeViewModel>()
    private var hMap: HuaweiMap? = null
    private lateinit var mapView: MapView
    private var mMarker: Marker? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val granted = permissions.entries.all { it.value }
        if (granted) {
            //Start tracking service
            startTrackingService()
        } else {
            Toast.makeText(this.context, "Please allow location for tracking", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun requestForLocationPermission() {
        val permissions = getPermissions()
        when {
            hasPermissions(this.requireContext(), *permissions) -> {
                //Start tracking service
                startTrackingService()
            }
            else -> {
                //Request for permissions
                requestPermissionLauncher.launch(
                    permissions
                )
            }
        }

    }

    /**
     * Returns an Array of String of the permissions required to start tracking
     *
     * Android Q and above requires additional permission [Manifest.permission.ACCESS_BACKGROUND_LOCATION]
     * to perform background tracking
     *
     */
    private fun getPermissions(): Array<String>{
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "Request permission from SDK <= 28 Q")
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        } else {
            Log.i(TAG, "Request permission from SDK > 28 Q")
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    private fun startTrackingService(){
        Intent(this.context, TrackeeGeoCoordService::class.java).also{
            this.context?.startService(it)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.userLocation.observe(viewLifecycleOwner) {
            Log.d(TAG, "User location ${it.toDMSFormat()}")
            addMarker(it)
        }
        viewModel.startTracking()
        viewModel.userDetails()

        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)

        MapsInitializer.setApiKey("CgB6e3x9y5c8sOhG0Fjd6i7dvnPOxF8+pKKWhXCG3N58byTVYR9uPaUK7LnoRP8mGhgg+hotmPazueze0597cUhU")

        binding.apply {
            this@HomeFragment.mapView = mapView
            mapView.onCreate(mapViewBundle)
            mapView.getMapAsync(this@HomeFragment)
        }

        // Request for permission
        requestForLocationPermission()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    override fun onMapReady(map: HuaweiMap?) {
//        Log.d(TAG, "onMapReady: ")
//    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = map
        hMap?.isMyLocationEnabled = false
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    fun addMarker(geoCoord: GeoCoord) {
        if (null != mMarker) {
            mMarker?.remove()
        }
        val latLng = geoCoord.toLatLng()
        val options = MarkerOptions()
            .position(latLng)
            .title("Hello Huawei Map")
            .snippet("This is a snippet!")
        mMarker = hMap?.addMarker(options)
        hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
    }

}