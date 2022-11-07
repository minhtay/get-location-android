package com.example.myapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.AdapterHistory.*
import com.example.myapp.databinding.ItemHistoryBinding

class AdapterHistory : RecyclerView.Adapter<Viewholder>() {
    class Viewholder(binding: ItemHistoryBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
       return Viewholder( ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}