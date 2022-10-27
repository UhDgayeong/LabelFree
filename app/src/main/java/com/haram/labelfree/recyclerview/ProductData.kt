package com.haram.labelfree.recyclerview

import com.google.firebase.storage.StorageReference

data class ProductData(
    val productImgUri: StorageReference,
    val productName : String,
    val productCompany : String
)
