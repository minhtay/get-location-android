package com.example.myapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.databinding.ActivityDetailHistoryBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class DetailHistoryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var data: LocationData
    private var point = ArrayList<LocationRuntimeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        data = intent.getSerializableExtra("dataDetail")!! as LocationData
        for (i in 0 until data.locationRuntime!!.size){
            data.locationRuntime!![i]?.let { point.add(it) }
        }
        Log.d("TAG", "onCreate: ")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var polyOptions = PolylineOptions()
        polyOptions.add(LatLng(data.latitudeStart!!,data.longitudeStart!!))
        for (i in 0 until point.size){
            polyOptions.add( LatLng(point[i].latitude!!,point[i].longitude!!))
        }
        polyOptions.width(5f).color(Color.RED)
        mMap.addPolyline(
            polyOptions
        )
        mMap.addMarker(MarkerOptions().position(LatLng(data.latitudeStart!!,data.longitudeStart!!)).title("Start"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(data.latitudeStart!!,data.longitudeStart!!), 15f))


    }

}

