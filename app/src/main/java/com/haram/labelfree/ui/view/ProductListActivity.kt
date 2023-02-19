package com.haram.labelfree.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityProductListBinding
import com.haram.labelfree.recyclerview.LabelData
import com.haram.labelfree.recyclerview.ProductData
import com.haram.labelfree.recyclerview.ProductRecyclerViewAdapter
import com.haram.labelfree.ui.custom.CustomItemDecoration
import com.haram.labelfree.ui.viewmodel.DrinkViewModel
import kotlinx.coroutines.runBlocking

// 제품명 리스트를 보여주는 액티비티
// 리스트에서 한 제품을 클릭하면 해당 제품 정보들을 보여주는 LabelInfoActivity로 연결됨
class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    val viewModel: DrinkViewModel by viewModels()
    val mDatas = mutableListOf<ProductData>()
    var drinkNameList = ArrayList<String>()
    var drinkCompanyList = ArrayList<String>()

    lateinit var mAdView : AdView

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

        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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