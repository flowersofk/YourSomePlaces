package com.flowersofk.yoursomeplaces.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.flowersofk.yoursomeplaces.data.PlaceProduct

class PlaceViewModel  : ViewModel() {

    private val repository: PlaceRepository = PlaceRepository()

    private val queryLiveData = MutableLiveData<Int>()

    private val response: LiveData<PlaceLiveData> = Transformations.map(queryLiveData, { repository.search(it) })

    val resultList: LiveData<PagedList<PlaceProduct>> = Transformations.switchMap(response, { it -> it.list })

    val networkErrors: LiveData<String> = Transformations.switchMap(response, { it -> it.networkErrors })

    val total_count: LiveData<Int> = Transformations.switchMap(response, { it -> it.count })

    fun requestPlaceList() {
        queryLiveData.postValue(1)
    }

}