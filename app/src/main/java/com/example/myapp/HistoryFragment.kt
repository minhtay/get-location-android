package com.example.myapp

import android.app.DatePickerDialog
import android.content.Intent
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
import kotlin.collections.ArrayList
import kotlin.math.log


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

        /*iniTView()
        getData()
        binding.btnCalendar.setOnClickListener {
            if (codeBtn == 0) {
                datePickerDiaolog()
                codeBtn = 1
            } else {
                clearCalendar()
                codeBtn = 0
            }
        }*/

        //test()
        test2()
    }

    private fun test2() {
        val ref = FirebaseFirestore.getInstance().collection("Location")
            ref.get().addOnSuccessListener {
                if (!it.isEmpty){
                    for (documentSnapshot in it) {
                        var date = documentSnapshot.id
                        readDataByDate(date)
                    }
                    Log.d("TAG", "test2: ")
                }else{
                    Log.d("TAG", "test2: ")
                }

            }
//            .collection("2022").get()
//            ref.addOnSuccessListener{
//                if (it.isEmpty){
//                    Log.d("TAG", "onViewCreated: ")
//                }else{
//                    Log.d("TAG", "onViewCreated: ")
//                    val ddi = ref
//                    ref.addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            for (document in task.result) {
//                                val data = document.toObject(LocationData::class.java)
//                                Log.d("TAG", "$data")
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.exception)
//                        }
//                    }
//                }
//            }
    }

    private fun readDataByDate(date: String) {
        val ref = FirebaseFirestore.getInstance().collection("Location").document(date)
        var data = ReponseRead()
        var data2 = ReponseUpload()
        ref.get().addOnCompleteListener {
            if (it.isSuccessful){
                val data3 = it.result.toObject(ReponseUpload::class.java)!!
                data2 = data3
                Log.d("TAG", "readDataByDate: ")
            }else{
                Log.d("TAG", "readDataByDate: ")

            }
            data = ReponseRead(date,data2)
            Log.d("TAG", "readDataByDate: ")
        }

    }

    private fun test() {
        /* ref.whereEqualTo("date","09-11-2022").get().addOnCompleteListener {
             if (it.isSuccessful){
                 Log.d("TAG", "onViewCreated: ")

             }else{
                 Log.d("TAG", "onViewCreated: ")
             }
         }.addOnFailureListener{
             Log.d("TAG", "onViewCreated: ")
         }*/

        val ref = FirebaseFirestore.getInstance().collection("Location")
            .whereEqualTo("date", "20-11-2022")

        ref.get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    Log.d("TAG", "test: ")
                    ref.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {
                                val result = task.result
                                val dataRef = result.toObjects(LocationData::class.java)
                                Log.d("TAG", document.id + " => " + document.data)

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.exception)
                        }
                    }
                } else {
                    Log.d("TAG", "test: ")
                }
            }


    }

    private fun clearCalendar() {
        binding.tvCalendar.text = "dd-MMM-yyyy"
        binding.btnClearCalendar.visibility = View.GONE
        adapter = AdapterHistory(requireActivity(), array) { data -> onclickItem(data) }
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
        adapter = AdapterHistory(requireActivity(), arrayFilter) { data -> onclickItem(data) }
        binding.rcvHistory.adapter = adapter

    }

    private fun iniTView() {
        adapter = AdapterHistory(requireActivity(), array) { data ->
            onclickItem(data)
        }
        binding.rcvHistory.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rcvHistory.adapter = adapter

    }

    private fun onclickItem(data: Any) {
        val dataBundle: LocationData = data as LocationData
        val intent = Intent(requireActivity(), DetailHistoryActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("dataDetail", dataBundle)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun getData() {
        FirebaseFirestore.getInstance().collection("Location")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    val dataRef = result.toObjects(LocationData::class.java)
                    /*array.addAll(dataRef)
                    adapter.notifyDataSetChanged()*/
                }

            }

            .addOnFailureListener {
                Log.d("TAG", "onViewCreated:$it ")

            }
    }


}