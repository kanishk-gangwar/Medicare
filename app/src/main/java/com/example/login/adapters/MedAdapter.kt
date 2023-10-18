package com.example.login.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.medicines

class MedAdapter(private var Medlist : List<medicines> ) :
    RecyclerView.Adapter<MedAdapter.MyViewHolder>(){

    private lateinit var mlistener : onItemClickListener

    fun submitList(newList: List<medicines>) {
        Medlist = newList
        notifyDataSetChanged()
    }

    init {
        this.Medlist = Medlist
    }

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mlistener = clickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_list, parent,false)
        return MedAdapter.MyViewHolder(itemView, mlistener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = Medlist[position]
        holder.MedName.text = currentitem.medname

    }

    override fun getItemCount(): Int {

        return Medlist.size
    }

    class MyViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val MedName : TextView = itemView.findViewById(R.id.medcineName)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}