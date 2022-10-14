package com.haram.labelfree.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.haram.labelfree.data.DrinkRepository

class DrinkViewModel : ViewModel() {
    var repository = DrinkRepository()

    // firestore에 저장된 데이터 불러오기
    suspend fun reload() {
        repository.getFirebaseData()
    }

    fun getData(): ArrayList<String> {
        return repository.getData()
    }
}