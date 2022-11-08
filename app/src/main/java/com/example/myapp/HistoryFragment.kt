package com.example.myapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.databinding.FragmentHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: AdapterHistory
    private var array = ArrayList<LocationData>()
    private var arrayFilter = ArrayList<LocationData>()
    private var codeBtn = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniTView()
        getData()
        binding.btnCalendar.setOnClickListener {
            if (codeBtn == 0) {
                datePickerDiaolog()
                codeBtn = 1
            } else {
                clearCalendar()
                codeBtn = 0
            }
        }
    }

    private fun clearCalendar() {
        binding.tvCalendar.text = "dd-MMM-yyyy"
        binding.btnClearCalendar.visibility = View.GONE
        adapter = AdapterHistory(requireActivity(), array)
        binding.rcvHistory.adapter = adapter
    }

    private fun datePickerDiaolog() {
        val mCurrentTime = Calendar.getInstance()
        val year = mCurrentTime.get(Calendar.YEAR)
        val month = mCurrentTime.get(Calendar.MONTH)
        val day = mCurrentTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireActivity(),
            { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth-${month + 1}-$year"
                val dateDefaut = "$dayOfMonth-${month}-$year"
                binding.tvCalendar.text = date
                binding.btnClearCalendar.visibility = View.VISIBLE
                filterData(date)
            }, year, month, day
        ).show()

    }

    private fun filterData(date: String) {
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = formatter.parse(date) as Date
        val dateLong = date.time
        val dateLong1 = dateLong + 86400000
//        arrayFilter = array.filter { s->
//            s.timeStar!! in dateLong until dateLong1
//        } as ArrayList<LocationData>
        arrayFilter =
            array.filter { s -> s.timeStar!! in dateLong until dateLong1 } as ArrayList<LocationData>
        Log.d("TAG", "filterData:  ")
        adapter = AdapterHistory(requireActivity(), arrayFilter)
        binding.rcvHistory.adapter = adapter

    }

    private fun iniTView() {
        adapter = AdapterHistory(requireActivity(), array)
        binding.rcvHistory.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rcvHistory.adapter = adapter

    }

    private fun getData() {
        FirebaseFirestore.getInstance().collection("Location")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    val dataRef = result.toObjects(LocationData::class.java)
                    array.addAll(dataRef)
                    adapter.notifyDataSetChanged()
                }

            }

            .addOnFailureListener {
                Log.d("TAG", "onViewCreated:$it ")

            }
    }


}