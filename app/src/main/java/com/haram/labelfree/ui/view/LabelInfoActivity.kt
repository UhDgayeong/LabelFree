package com.haram.labelfree.ui.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
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
    lateinit var nameTxt : TextView

    lateinit var date2 : TextView
    lateinit var ml2 : TextView
    lateinit var cal2 : TextView
    lateinit var raw2 : TextView
    lateinit var caution2 : TextView
    lateinit var type2 : TextView
    lateinit var company2 : TextView
    lateinit var manufac2 : TextView
    lateinit var contmat2 : TextView
    lateinit var source : TextView

    lateinit var mAdView : AdView

    private lateinit var docName : String

    val productList = ArrayList<Map<String, String>>()

    val viewModel: DrinkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLabelInfoBinding>(this, R.layout.activity_label_info)

        docName = intent.getStringExtra("Name").toString() // 메인액티비티에서 검색한 것
        viewModel.getDrinkInfoMap(docName)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        //binding.viewModel.getDrinkInfoMap(docName)

//        runBlocking {
//            //getRepoData()
//            //viewModel.reload()
//            binding.viewModel = viewModel
//        }



        //viewModel.getDrinkInfoMap(docName)
        //viewModel.productMap = viewModel.getDrinkInfoMap(docName)



        //val storageRef = Firebase.storage.reference.child(docName + ".png")
        val storageRef = viewModel.getDrinkImgRef(docName)

        // Ad
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        nameTxt = findViewById(R.id.nameTxt)
        //nameTxt.text = docName

        image = findViewById(R.id.image)
        storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(image)
            }
        })

        barChart = findViewById(R.id.chart)
        barChart.setNoDataText("")

        //val productList = ArrayList<Map<String, String>>()

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            finish()
        }

        date2 = findViewById(R.id.date2)
        ml2 = findViewById(R.id.ml2)
        cal2 = findViewById(R.id.cal2)
        raw2 = findViewById(R.id.raw2)
        caution2 = findViewById(R.id.caution2)
        type2 = findViewById(R.id.type2)
        company2 = findViewById(R.id.company2)
        manufac2 = findViewById(R.id.manufac2)
        contmat2 = findViewById(R.id.contmat2)
        source = findViewById(R.id.sourceTxt)


        getData()
    } //onCreate

    private fun getData() {
        db.collection("drinks")
            .whereEqualTo("제품명", docName)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    productList.add(mapOf(
                        "productName" to doc.data["제품명"].toString(),
                        "carbo" to doc.data["탄수화물"].toString(),
                        "sugar" to doc.data["당류"].toString(),
                        "natrium" to doc.data["나트륨"].toString(),
                        "fat" to doc.data["지방"].toString(),
                        "protein" to doc.data["단백질"].toString(),
                        "date" to doc.data["유통기한"].toString(),
                        "ml" to doc.data["총 내용량"].toString(),
                        "cal" to doc.data["총 칼로리"].toString(),
                        "raw" to doc.data["원재료명"].toString(),
                        "caution" to doc.data["주의사항"].toString(),
                        "type" to doc.data["식품유형"].toString(),
                        "company" to doc.data["제조회사"].toString(),
                        "manufac" to doc.data["제조원"].toString(),
                        "contmat" to doc.data["용기재질"].toString(),
                        "source" to doc.data["출처"].toString()
                    ))
                }

                for (pd in productList) {
                    //Log.d(TAG,"탄수화물 : ${pd["cargo"]} / 당류 : ${pd["sugar"]} / 나트륨 : ${pd["natrium"]} / 지방 : ${pd["fat"]} / 단백질 : ${pd["protein"]}")
                    if (pd["productName"] == docName) {
                        Log.d(TAG, "size : ${productList.size}")
                        Log.d(TAG,"탄수화물 : ${pd["carbo"]} / 당류 : ${pd["sugar"]} / 나트륨 : ${pd["natrium"]} / 지방 : ${pd["fat"]} / 단백질 : ${pd["protein"]}")
                        carbo = pd["carbo"]?.toFloat() ?: 0F
                        sugar = pd["sugar"]?.toFloat() ?: 0F
                        natrium = pd["natrium"]?.toFloat() ?: 0F
                        fat = pd["fat"]?.toFloat() ?: 0F
                        protein = pd["protein"]?.toFloat() ?: 0F

                        date2.text = pd["date"]
                        ml2.text = pd["ml"]
                        cal2.text = pd["cal"]
                        raw2.text = pd["raw"]
                        caution2.text = pd["caution"]
                        type2.text = pd["type"]
                        company2.text = pd["company"]
                        manufac2.text = pd["manufac"]
                        contmat2.text = pd["contmat"]
                        source.text = "출처 : ${pd["source"]}"

                        val entryList = mutableListOf<BarEntry>()
                        entryList.add(BarEntry(4f, round((carbo/324)*100)))
                        entryList.add(BarEntry(3f, round((sugar/100)*100)))
                        entryList.add(BarEntry(2f, round((natrium/2000)*100)))
                        entryList.add(BarEntry(1f, round((fat/54)*100)))
                        entryList.add(BarEntry(0f, round((protein/55)*100)))

                        makeGraph(entryList)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
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