package com.haram.labelfree.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.haram.labelfree.data.DrinkRepository

class DrinkViewModel : ViewModel() {
    var repository = DrinkRepository()

    var productMap = mapOf<String, String>()

    // firestore에 저장된 데이터 불러오기
    suspend fun reload() {
        repository.getFirebaseData()
    }

    fun getDrinkNameList(): ArrayList<String> {
        return repository.getDrinkNameList()
    }

    fun getDrinkCompanyList(): ArrayList<String> {
        return repository.getDrinkCompanyList()
    }

    // 매개변수로 제품명이 주어지면 해당 제품의 정보들을 불러오는 메소드
    suspend fun getDrinkInfoMap(docName: String) {
        repository.getDrinkInfo(docName)
        productMap = repository.getDrinkInfoMap()
    }

    // 매개변수로 제품명이 주어지면 해당 제품의 사진을 불러오는 메소드
    fun getDrinkImgRef(docName: String): StorageReference {
        return Firebase.storage.reference.child(docName + ".png")
    }
}