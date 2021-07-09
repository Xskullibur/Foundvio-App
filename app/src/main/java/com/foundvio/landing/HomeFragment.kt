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
import com.foundvio.utils.isSuccess
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.userDetails()

        val mapViewBundle  = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)

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
//        hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
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

}