package com.flowersofk.yoursomeplaces.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flowersofk.yoursomeplaces.data.PlaceProduct

class PlaceFavoriteViewModel : ViewModel() {

    val favoritesLiveData = MutableLiveData<List<PlaceProduct>>()

    fun getFavoritesPlace(list: List<PlaceProduct>) {
        favoritesLiveData.postValue(list)
    }

}