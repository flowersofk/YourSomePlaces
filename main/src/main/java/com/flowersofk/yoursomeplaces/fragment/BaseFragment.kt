package com.flowersofk.yoursomeplaces.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.flowersofk.yoursomeplaces.db.helper.PlaceFavoriteDbHelper

open class BaseFragment : Fragment() {

    lateinit var dbHelper: PlaceFavoriteDbHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = Room.databaseBuilder(requireActivity(), PlaceFavoriteDbHelper::class.java, "room_place")
            .allowMainThreadQueries()
            .build()

    }

}