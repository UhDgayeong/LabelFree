package com.example.labelfree

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.shadow.ShadowRenderer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.roundToInt

class Product(
    var name:String = "",
    var fat:Float = 0F,
    var cargo:Float = 0F,
    var protein:Float = 0F,
    var sugar:Float = 0F,
    var natrium:Float = 0F,
)

class LabelInfoActivity : AppCompatActivity() {

    private val TAG = "LabelInfoTest"
    private val db = Firebase.firestore
    private val docName = "코카콜라 제로"

    private var carbo : Float = 0F
    private var sugar : Float = 0F
    private var natrium : Float = 0F
    private var fat : Float = 0F
    private var protein : Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_label_info)

        val carboTxt = findViewById<TextView>(R.id.carboTxt)
        val sugarTxt = findViewById<TextView>(R.id.sugarTxt)
        val natriumTxt = findViewById<TextView>(R.id.natriumTxt)
        val fatTxt = findViewById<TextView>(R.id.fatTxt)
        val proteinTxt = findViewById<TextView>(R.id.proteinTxt)



        val productList = ArrayList<Map<String, Any>>()

        db.collection("drinks").get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    productList.add(mapOf(
                        "productName" to doc.data["제품명"].toString(),
                        "cargo" to doc.data["탄수화물"].toString().toFloat(),
                        "sugar" to doc.data["당류"].toString().toFloat(),
                        "natrium" to doc.data["나트륨"].toString().toFloat(),
                        "fat" to doc.data["지방"].toString().toFloat(),
                        "protein" to doc.data["단백질"].toString().toFloat(),
                    ))
                    // Log.d(TAG, "onCreate: ${doc.data}")
                }

                for (pd in productList) {
                    Log.d(TAG,"탄수화물 : ${pd["cargo"]} / 당류 : ${pd["sugar"]} / 나트륨 : ${pd["natrium"]} / 지방 : ${pd["fat"]} / 단백질 : ${pd["protein"]}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        //carboTxt.text = productList[]

//        db.collection("drinks")
//            .whereEqualTo("제품명", docName)
//            .get()
//            .addOnSuccessListener { result ->
//                for (doc in result) {
//                    //Log.d(TAG, "${doc["나트륨"]} => ${doc["탄수화물"]}")
//                    carboTxt.text = doc["탄수화물"].toString()
//                    carbo = doc["탄수화물"].toString().toFloat()
//
//                    sugarTxt.text = doc["당류"].toString()
//                    sugar = doc["당류"].toString().toFloat()
//
//                    natriumTxt.text = doc["나트륨"].toString()
//                    natrium = doc["나트륨"].toString().toFloat()
//
//                    fatTxt.text = doc["지방"].toString()
//                    fat = doc["지방"].toString().toFloat()
//
//                    proteinTxt.text = doc["단백질"].toString()
//                    protein = doc["단백질"].toString().toFloat()
//
//                    Log.d(TAG,"탄수화물 : ${carbo} / 당류 : ${sugar} / 나트륨 : ${natrium} / 지방 : ${fat} / 단백질 : ${protein}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }

        val entryList = mutableListOf<BarEntry>()
        entryList.add(BarEntry(4f, 10f))
        entryList.add(BarEntry(3f, 20f))
        entryList.add(BarEntry(2f, 30f))
        entryList.add(BarEntry(1f, 40f))
        entryList.add(BarEntry(0f, 50f))
//        entryList.add(BarEntry(0f,1f))
//        entryList.add(BarEntry(1f,5f))
//        entryList.add(BarEntry(2f,0f))

        val barDataSet = BarDataSet(entryList, "MyBarDataSet")

        val colorList = listOf(
            Color.rgb(216, 117, 248),
            Color.rgb(152, 118, 255),
            Color.rgb(115, 148, 255),
            Color.rgb(70, 202, 224),
            Color.rgb(112, 222, 117)

        )

        barDataSet.colors = colorList
            //ColorTemplate.rgb("#4CB7EB")
        barDataSet.valueTextSize = 15f // 값 숫자 크기
        //barDataSet.valueFormatter = DefaultValueFormatter(0)


        val barData = BarData(barDataSet)
        barData.barWidth = 0.6f // 바 두께
        //barData.setValueFormatter(DefaultValueFormatter(0))
        barData.setValueFormatter(PercentFormatter())

        val barChart = findViewById<HorizontalBarChart>(R.id.chart)
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
            animateX(1500)

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

            setNoDataText("표시할 데이터가 없습니다")
        } //barChart



    } //onCreate

    /*

    private fun initBarDataSet(barDataSet: BarDataSet) {
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false)
        //setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f
    }

    private fun setWeek(barChart: BarChart, entryList: MutableList<BarEntry>) {
        initBarChart(barChart)

        barChart.setScaleEnabled(false) //Zoom In/Out

        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        //val title = "걸음 수"

        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entryList, "")
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()
    }*/
}