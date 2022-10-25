package com.haram.labelfree.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haram.labelfree.R

class ProductRecyclerViewAdapter: RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>() {

    var dataList = mutableListOf<ProductData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_product_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var productNameTxt: TextView
        var productCompanyTxt: TextView

        init {
            productNameTxt = itemView.findViewById(R.id.productNameTxt)
            productCompanyTxt = itemView.findViewById(R.id.productCompanyTxt)
        }

        fun bind(productData: ProductData) {
            productNameTxt.text = productData.productName
            productCompanyTxt.text = productData.productCompany
        }
    }


}