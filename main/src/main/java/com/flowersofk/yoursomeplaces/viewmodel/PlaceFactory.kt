package com.flowersofk.yoursomeplaces.viewmodel


import PlaceDataSource
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.api.SearchPlaceService

class PlaceFactory (
    private val page: Int,
    private val service: SearchPlaceService
) : DataSource.Factory<Int, PlaceProduct>() {

//    private lateinit var placeDataSource: PlaceDataSource
//    var liveData = MutableLiveData<PlaceDataSource>()

    override fun create(): DataSource<Int, PlaceProduct> {

//        val placeDataSource = PlaceDataSource(page, service)
//        liveData.postValue(placeDataSource)

        return PlaceDataSource(page, service)
    }

}