package com.foundvio.landing

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.foundvio.R
import com.foundvio.databinding.FragmentHomeBinding
import com.foundvio.service.UserService
import com.foundvio.tracking.GeoCoord
import com.foundvio.tracking.toLatLng
import com.foundvio.utils.isSuccess
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