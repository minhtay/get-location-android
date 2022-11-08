package com.example.myapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var codeBtn = 0
    private var codeRequest = 0
    private lateinit var runnable: Runnable
    var ret: Long = 0
    private lateinit var dateString: String
    private var dateLong: Long = 0
    private lateinit var id: String
    private lateinit var handler: Handler


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

        handler = Handler()
        runnable = Runnable() {
            getLocation()
            handler.postDelayed(runnable, 10000)
            binding.code.text = codeRequest.toString()
        }

        binding.button.setOnClickListener {
            getTimeLocal()
            if (codeBtn == 0) {
                binding.button.setText(com.example.myapp.R.string.btn_stop)
                codeBtn = 1
                handler.post(runnable)
                Log.d("TAG", "click: ")
            } else {
                binding.button.setText(com.example.myapp.R.string.btn_start)
                codeBtn = 0
                codeRequest = 0
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT)
                        .show()
                else {
                    val lat = location.latitude
                    val lon = location.longitude
                    binding.latitude.text = lat.toString()
                    binding.longitude.text = lon.toString()
                    if (codeBtn == 1) {
                        if (codeRequest == 0) {
                            uploadStart(lat, lon)
                        } else {
                            uploadRuntime(lat, lon)
                        }
                    } else {
                        finishUpload(lat, lon)
                    }
                }

            }


    }

    private fun finishUpload(lat: Double, lon: Double) {
        val idd = UUID.randomUUID().toString()
        val data = LocationRuntimeData(idd, lat, lon, System.currentTimeMillis())
        FirebaseDatabase.getInstance().getReference("Location/${dateString}/$id")
            .child(idd).setValue(data)
        handler.removeCallbacks(runnable)
        binding.latitude.text = ""
        binding.longitude.text = ""
        binding.code.text = ""
        binding.date.text = ""
    }

    private fun uploadRuntime(lat: Double, lon: Double) {
        val idd = UUID.randomUUID().toString()
        val data = LocationRuntimeData(idd, lat, lon, System.currentTimeMillis())
//        FirebaseDatabase.getInstance().getReference("Location/${dateString}/$id").child(idd)
//            .setValue(data)
        val db = FirebaseFirestore.getInstance().collection("Location")
        db.document(id).update("locationRuntime", FieldValue.arrayUnion(data))
        codeRequest++
    }

    private fun uploadStart(lat: Double, lon: Double) {
        val db = FirebaseFirestore.getInstance().collection("Location")
        id = db.document().id
        val data = LocationData(id, dateString, dateLong, lat, lon, null)
        db.document(id).set(data)
            .addOnSuccessListener {
                codeRequest++
                Log.d("TAG", "uploadStart: Success")
            }
            .addOnFailureListener {
                Log.d("TAG", "uploadStart: Failure")
            }
    }

    fun getTimeLocal() {
        dateLong = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MMMM-dd")
        dateString = dateFormat.format(dateLong).toString()
        binding.date.text = dateString
    }

}

