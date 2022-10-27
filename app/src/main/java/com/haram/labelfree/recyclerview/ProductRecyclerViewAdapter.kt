package com.haram.labelfree.recyclerview

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haram.labelfree.R
import com.haram.labelfree.ui.view.LabelInfoActivity
import java.util.*

class ProductRecyclerViewAdapter: RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>() {

    var dataList = mutableListOf<ProductData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_product_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, LabelInfoActivity::class.java)
            intent.putExtra("Name", holder.productNameTxt.text)
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var productImg: ImageView
        var productNameTxt: TextView
        var productCompanyTxt: TextView

        init {
            productImg = itemView.findViewById(R.id.productImg)
            productNameTxt = itemView.findViewById(R.id.productNameTxt)
            productCompanyTxt = itemView.findViewById(R.id.productCompanyTxt)
        }

        fun bind(productData: ProductData) {
            Log.d("imgload", productData.productImgUri.toString())

            val storageRef = productData.productImgUri
            storageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(itemView)
                        .load(task.result).into(productImg)
                }
            }
            Glide.with(itemView)
                .load(productData.productImgUri).into(productImg)
            productNameTxt.text = productData.productName
            productCompanyTxt.text = productData.productCompany
        }
    }


}