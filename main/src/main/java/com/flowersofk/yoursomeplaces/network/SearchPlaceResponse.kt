package com.flowersofk.yoursomeplaces.network

import com.flowersofk.yoursomeplaces.data.PlaceData

data class SearchPlaceResponse(

    val msg: String,
    val data: PlaceData,
    val code: Int

)