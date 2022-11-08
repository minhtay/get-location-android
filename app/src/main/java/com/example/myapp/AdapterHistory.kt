package com.example.myapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.AdapterHistory.*
import com.example.myapp.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterHistory(private val activity: Context, private val arrayList: ArrayList<LocationData>) : RecyclerView.Adapter<Viewholder>() {
    class Viewholder(val binding: ItemHistoryBinding) :RecyclerView.ViewHolder(binding.root)

    private lateinit var adapter : AdapterRuntime

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
       return Viewholder( ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        var data: LocationData = arrayList[position]

        holder.binding.date.text = data.date
        holder.binding.latitudeStart.text = data.latitudeStart.toString()
        holder.binding.longitudeStart.text = data.longitudeStart.toString()
        val dateFormat = SimpleDateFormat("yyyy-MMMM-dd", Locale.getDefault())
        //holder.binding.timeStar.text =  dateFormat.format(data.timeStar).toString()
        holder.binding.timeStar.text =  data.timeStar.toString()

        var dataRuntime = ArrayList<LocationRuntimeData>()
        data.locationRuntime?.let { dataRuntime.addAll(it) }

        adapter = AdapterRuntime(activity,dataRuntime)
        holder.binding.runtime.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        holder.binding.runtime.adapter = adapter
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}