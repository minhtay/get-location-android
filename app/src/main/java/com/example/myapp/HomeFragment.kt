package com.example.myapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var codeBtn = 0
    private var codeRequest = 0
    private var codeEvent = 0
    private lateinit var runnable: Runnable
    var ret: Long = 0
    private lateinit var dateString: String
    private var dateLong: Long = 0
    private lateinit var id: String
    private lateinit var handler: Handler
    private var locationRequest: Location? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ServiceCast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()

        binding.btnAction.setOnClickListener {
            test()
        }
    }

    private fun test() {
        val id1 = UUID.randomUUID().toString()
        val id2 = UUID.randomUUID().toString()
        val data = LocationData(id1,"20-11-2022",2,2.0,2.0,null)
        val arr = ArrayList<LocationData>()
        arr.add(data)
        val up = ReponseUpload(arr)

        val ref = FirebaseFirestore.getInstance().collection("Location").document("2202")
        ref.set(up)



    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    locationRequest = location
                    when (codeEvent) {
                        0 -> defautLocation(location)
                        1 -> startLocation(location)
                        2 -> runtimeLocation(location)
                        3 -> finishLocation(location)
                    }
                }
            }
        Log.d("TAG", "getLocation: ")
    }

    private fun defautLocation(location: Location) {
        binding.data.text = "${location.latitude} : ${location.longitude}"
        binding.data.visibility = View.VISIBLE

    }

    private fun startLocation(location: Location) {
        val ref = FirebaseFirestore.getInstance().collection("Test").document("Location")
            .collection(dateString).get()
        ref.addOnSuccessListener {
            if (it.isEmpty) {
            } else {
            }
        }
    }

    private fun runtimeLocation(location: Location) {
        TODO("Not yet implemented")
    }

    private fun finishLocation(location: Location) {

    }
}