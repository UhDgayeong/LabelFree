package com.haram.labelfree.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityProductListBinding
import com.haram.labelfree.recyclerview.LabelData
import com.haram.labelfree.recyclerview.ProductData
import com.haram.labelfree.recyclerview.ProductRecyclerViewAdapter
import com.haram.labelfree.ui.custom.CustomItemDecoration
import com.haram.labelfree.ui.viewmodel.DrinkViewModel
import kotlinx.coroutines.runBlocking

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    val viewModel: DrinkViewModel by viewModels()
    val mDatas = mutableListOf<ProductData>()
    var drinkNameList = ArrayList<String>()
    var drinkCompanyList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_list)

        runBlocking {
            viewModel.reload()
        }

        drinkNameList = viewModel.getDrinkNameList()
        drinkCompanyList = viewModel.getDrinkCompanyList()
        initRecyclerViewList()
        initProductRecyclerView()

    }

    fun initProductRecyclerView() {
        val adapter = ProductRecyclerViewAdapter()
        adapter.dataList = mDatas

        binding.plRecyclerView.adapter = adapter
        binding.plRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.plRecyclerView.addItemDecoration(CustomItemDecoration())
    }

    fun initRecyclerViewList() {
        for (d in drinkNameList) {
            mDatas.add(ProductData(viewModel.getDrinkImgRef(d), d, drinkCompanyList[drinkNameList.indexOf(d)]))
        }
    }
}