package com.haram.labelfree.ui.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.haram.labelfree.R
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.haram.labelfree.databinding.ActivityLabelInfoBinding
import com.haram.labelfree.recyclerview.LabelData
import com.haram.labelfree.recyclerview.LabelRecyclerViewAdapter
import com.haram.labelfree.ui.viewmodel.DrinkViewModel
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.math.round

class LabelInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLabelInfoBinding
    private val TAG = "LabelInfoTest"
    private val db = Firebase.firestore

    //private val docName = "칠성사이다 ECO"
    //private val docName = "코카콜라 제로"

    private var carbo : Float = 0F
    private var sugar : Float = 0F
    private var natrium : Float = 0F
    private var fat : Float = 0F
    private var protein : Float = 0F

    lateinit var barChart : HorizontalBarChart
    lateinit var image : ImageView

    lateinit var mAdView : AdView

    private lateinit var docName : String

    val mDatas = mutableListOf<LabelData>()

    val viewModel: DrinkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLabelInfoBinding>(this, R.layout.activity_label_info)

        docName = intent.getStringExtra("Name").toString() // 메인액티비티에서 검색한 것
        //binding.viewModel = viewModel

        binding.lifecycleOwner = this // LiveData를 Databinding으로 쓸 경우 써 줘야 함
        //binding.viewModel.getDrinkInfoMap(docName)

        runBlocking {
            //getRepoData()
            //viewModel.reload()
            viewModel.getDrinkInfoMap(docName)
            //binding.viewModel = viewModel
        }

        binding.viewModel = viewModel
        Log.d("pmTesting", viewModel.productMap.toString())


        //viewModel.getDrinkInfoMap(docName)
        //viewModel.productMap = viewModel.getDrinkInfoMap(docName)



        //val storageRef = Firebase.storage.reference.child(docName + ".png")
        val storageRef = viewModel.getDrinkImgRef(docName)

        // Ad
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        image = findViewById(R.id.image)
        storageRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(image)
            }
        }

        barChart = findViewById(R.id.chart)
        barChart.setNoDataText("")

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            finish()
        }

        getGraphData()

        initRecyclerViewList()
        initLabelRecyclerView()
    } //onCreate

    fun initLabelRecyclerView() {
        val adapter = LabelRecyclerViewAdapter()
        adapter.dataList = mDatas // 데이터 넣기
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun initRecyclerViewList() {
        val pm = viewModel.productMap
        with(mDatas) {
            add(LabelData("유통기한", pm["date"].toString()))
            add(LabelData("총 내용량", pm["ml"].toString()))
            add(LabelData("총 칼로리", pm["cal"].toString()))
            add(LabelData("원재료명", pm["raw"].toString()))
            add(LabelData("주의사항", pm["caution"].toString()))
            add(LabelData("식품유형", pm["type"].toString()))
            add(LabelData("제조회사", pm["company"].toString()))
            add(LabelData("제조원", pm["manufac"].toString()))
            add(LabelData("용기재질", pm["contmat"].toString()))
        }
    }

    private fun getGraphData() {
        val entryList = mutableListOf<BarEntry>()

        Log.d("carbotest", viewModel.productMap["carbo"] ?: "empty")
        Log.d("testViewmodel", viewModel.productMap.toString())
        carbo = viewModel.productMap["carbo"]?.toFloat() ?: 0F
        sugar = viewModel.productMap["sugar"]?.toFloat() ?: 0F
        natrium = viewModel.productMap["natrium"]?.toFloat() ?: 0F
        fat = viewModel.productMap["fat"]?.toFloat() ?: 0F
        protein = viewModel.productMap["protein"]?.toFloat() ?: 0F

        entryList.add(BarEntry(4f, round((carbo/324)*100)))
        entryList.add(BarEntry(3f, round((sugar/100)*100)))
        entryList.add(BarEntry(2f, round((natrium/2000)*100)))
        entryList.add(BarEntry(1f, round((fat/54)*100)))
        entryList.add(BarEntry(0f, round((protein/55)*100)))

        makeGraph(entryList)
    }

    private fun makeGraph(entryList : List<BarEntry>) {

        val barDataSet = BarDataSet(entryList, "MyBarDataSet")

        barChart.setExtraOffsets(40F, 10F, 10F, 10F)
        val colorList = listOf(
            Color.rgb(216, 117, 248),
            Color.rgb(152, 118, 255),
            Color.rgb(115, 148, 255),
            Color.rgb(70, 202, 224),
            Color.rgb(112, 222, 117)

        )

        barDataSet.colors = colorList
        barDataSet.valueTextSize = 15f // 값 숫자 크기
        //barDataSet.valueFormatter = DefaultValueFormatter(0)


        val barData = BarData(barDataSet)
        barData.barWidth = 0.6f // 바 두께
        //barData.setValueFormatter(DefaultValueFormatter(0))
        barData.setValueFormatter(PercentFormatter())


        barChart.data = barData
        barChart.invalidate()


        val labels = arrayOf("단백질", "지방", "나트륨", "당류", "탄수화물")
        barChart.apply {
            //터치, Pinch 상호작용
            setScaleEnabled(false)
            setPinchZoom(false)
            isClickable = false

            //Chart 밑에 description 표시 유무
            description=null

            //Legend는 차트의 범례를 의미합니다
            //범례가 안 보이게 설정
            legend.isEnabled = false

            //X, Y 바의 애니메이션 효과
            animateY(1500)
            //animateX(2000)

            xAxis.apply {
                //xAxis, yAxis 둘다 존재하여 따로 설정이 가능합니다
                //차트 내부에 Grid 표시 유무
                setDrawGridLines(false)

                //x축 데이터 표시 위치
                position = XAxis.XAxisPosition.BOTTOM

                //x축 데이터 갯수 설정
                labelCount = 5
                valueFormatter = object: ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt()]
                    }
                }
                textSize = 15f
                setDrawAxisLine(false)

                //setDrawBarShadow(true)
            }

            axisLeft.apply {
                setDrawAxisLine(false)
                textSize = 30f
                setDrawLabels(false)
                isEnabled = false
            }

            axisRight.apply {
                // hiding the right y-axis line
                setDrawAxisLine(false)
                textSize = 30f
                //기본적으로 차트 우측 축에도 데이터가 표시됩니다
                //이를 활성화/비활성화 하기 위함
                //setDrawLabels(false)
                isEnabled = false
            }

            //차트의 좌, 우측 최소/최대값을 설정합니다.
            //차트 제일 밑이 0부터 시작하고 싶은 경우 설정합니다.
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 100f
            axisRight.axisMinimum = 0f
        } //barChart
    }
} // class