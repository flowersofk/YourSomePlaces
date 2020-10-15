package com.flowersofk.yoursomeplaces.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.flowersofk.yoursomeplaces.data.PlaceProduct

@Dao
interface PlaceFavoriteDao {

    // 즐겨찾기 전체목록
    @Query("SELECT * FROM place_favorites")
    fun getAllFavorites() : List<PlaceProduct>

    // 즐겨찾기 평점 정렬
    @Query("SELECT * FROM place_favorites ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN rate END ASC, " +
            "CASE WHEN :isAsc = 0 THEN rate END DESC")
    fun getRateSortFavorites(isAsc: Int) : List<PlaceProduct>

    // 즐겨찾기 등록시각 정렬
    @Query("SELECT * FROM place_favorites ORDER BY timeStamp DESC")
    fun getDateSortFavorites() : List<PlaceProduct>

    // 즐겨찾기 검색
    @Query("SELECT * FROM place_favorites WHERE id = :placeId")
    fun getSelectPlace(placeId : Int): PlaceProduct

    // 즐겨찾기 등록
    @Insert(onConflict = REPLACE)
    fun insert(place : PlaceProduct)

    // 즐겨찾기 등록(타임스탬프)
    fun insertWithTimestamp(place : PlaceProduct) {
        insert(place.apply{
            timeStamp = System.currentTimeMillis()
        })
    }

    @Delete
    fun delete(place : PlaceProduct)



}