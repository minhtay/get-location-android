package com.example.myapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.ItemRuntimeBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterRuntime (private val activity: Context, private val arrayList: ArrayList<LocationRuntimeData>) : RecyclerView.Adapter<AdapterRuntime.Viewholder>(){
    class Viewholder(val binding: ItemRuntimeBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(ItemRuntimeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data : LocationRuntimeData = arrayList[position]

        val dateFormat = SimpleDateFormat("yyyy-MMMM-dd", Locale.getDefault())
        holder.binding.time.text = dateFormat.format(data.time).toString()
        holder.binding.longitude.text = data.longitude.toString()
        holder.binding.latitude.text = data.latitude.toString()
        holder.binding.id.text = data.id.toString()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}