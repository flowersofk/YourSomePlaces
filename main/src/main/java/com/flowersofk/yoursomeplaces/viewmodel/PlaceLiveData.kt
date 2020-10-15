package com.flowersofk.yoursomeplaces.viewmodel

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.flowersofk.yoursomeplaces.data.PlaceProduct

/**
 * LiveData 정의
 */
data class PlaceLiveData(

    val count: LiveData<Int>,
    val list: LiveData<PagedList<PlaceProduct>>,
    val networkErrors: LiveData<String>

)