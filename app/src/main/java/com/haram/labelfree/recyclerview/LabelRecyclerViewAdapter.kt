package com.haram.labelfree.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haram.labelfree.R

class LabelRecyclerViewAdapter: RecyclerView.Adapter<LabelRecyclerViewAdapter.ViewHolder>() {

    var dataList = mutableListOf<LabelData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_label_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemTitle = itemView.findViewById(R.id.itemTitle)
            itemDetail = itemView.findViewById(R.id.itemDetail)
        }

        fun bind(labelData: LabelData) {
            itemTitle.text = labelData.itemTitle
            itemDetail.text = labelData.itemDetail
        }

    }
}