package com.uttamsaha.emergencyambulance

import android.content.res.Resources
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.jar.Manifest

class OperatorHomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var locationRequest : com.google.android.gms.location.LocationRequest
    private lateinit var locationCallback : LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_operator_home, container, false)

        init()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return root
    }

    private fun init() {
        locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority
        locationRequest.setFastestInterval(3000)
        locationRequest.interval = 5000
        locationRequest.setSmallestDisplacement(10f)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val newPos = LatLng(locationResult.lastLocation!!.latitude,locationResult.lastLocation!!.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos,18f))
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Dexter.withContext(requireContext()).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true
                    mMap.setOnMyLocationClickListener {
                        fusedLocationProviderClient.lastLocation
                            .addOnFailureListener { e->
                                Toast.makeText(context!!, e.message,Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener { location->
                                val userLatLng = LatLng(location.latitude,location.longitude)
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,18f))
                            }
                        true
                    }

                    mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    val view = mapFragment!!.requireView()
                        .findViewById<View>("1".toInt())!!
                        .parent!! as View
                    val locationButton = view.findViewById<View>("2".toInt());
                    val params = locationButton.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.ALIGN_TOP,0)
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
                    params.bottomMargin = 50
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(context!!, "Permission"+p0!!.permissionName+"was denied",Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {

                }

            }).check()
        try {
            val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.ambulance_maps_style))
            if(!success)
                Log.e("EDMT_ERROR","Style parsing error")
        }catch (e: Resources.NotFoundException){
            e.message?.let { Log.e("EDMT_ERROR", it) }
        }
    }
}