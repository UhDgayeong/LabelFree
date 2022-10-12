package com.haram.labelfree.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.haram.labelfree.data.DrinkRepository

class DrinkViewModel : ViewModel() {
    var repository = DrinkRepository()

    init {
        // 뷰모델 객체 생성 시 데이터 불러옴
        reload()
    }

    // firestore에 저장된 데이터 불러오기
    fun reload() {
        repository.getData()
    }
}