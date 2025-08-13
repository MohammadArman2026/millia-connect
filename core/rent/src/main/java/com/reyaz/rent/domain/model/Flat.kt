package com.reyaz.rent.domain.model

import java.util.Date

data class Flat(
    val id: String,
    val houseType : String ="FLAT",
    val bhk: Int,
    val amenities: List<String>,
    val postDate: Date
)
