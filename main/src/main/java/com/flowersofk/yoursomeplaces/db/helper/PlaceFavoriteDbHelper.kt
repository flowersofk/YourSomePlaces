package com.flowersofk.yoursomeplaces.db.helper

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.db.dao.PlaceFavoriteDao
import com.flowersofk.yoursomeplaces.utils.Converters

@Database(entities = arrayOf(PlaceProduct::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PlaceFavoriteDbHelper : RoomDatabase() {

    abstract fun placeFavoriteDao() : PlaceFavoriteDao

}