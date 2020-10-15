package com.flowersofk.yoursomeplaces.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.flowersofk.yoursomeplaces.utils.Constants.PAGE_SIZE
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.api.SearchPlaceService
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PlaceRepository {

    private val service = SearchPlaceService.create()
    private val executor: Executor = Executors.newFixedThreadPool(5)

    fun search(page: Int): PlaceLiveData {

        val dataSourceFactory = PlaceFactory(page, service)

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)                 // 페이지 기본 사이즈
            .setInitialLoadSizeHint(PAGE_SIZE)      // 최초 호출시 사이즈
            .setPrefetchDistance(5)                 // 호출 시점이 맨 마지막 행에서 얼마만큼 떨어져있는지 설정(ex. 10이면 맨 마지막에서 위로 10번재 스크롤시점에 호출
            .setEnablePlaceholders(true)
            .build()

        // 라이브 데이터 생성
        val list: LiveData<PagedList<PlaceProduct>> = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()

//        val networkErrors = Transformations.switchMap(dataSourceFactory, { dataSource -> dataSource.networkErrors })
//        val total_count = Transformations.switchMap(dataSourceFactory.liveData, { dataSource -> dataSource.countData})

        val total_count: LiveData<Int> = MutableLiveData()
        val networkErrors: LiveData<String> = MutableLiveData()

        return PlaceLiveData(total_count, list, networkErrors)

    }

}