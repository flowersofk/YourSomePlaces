package com.flowersofk.yoursomeplaces.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "place_favorites")
data class PlaceProduct(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int,                            // 숙소 Id

    @ColumnInfo
    val name: String,                       // 숙소명

    @ColumnInfo
    val thumbnail: String,                  // 객실 썸네일

    @ColumnInfo
    val rate: Float,                        // 평점

    @Embedded
    val description: PlaceDescription,      // 설명

    @ColumnInfo
    var timeStamp: Long                     // 즐겨찾기 등록시간

) : Parcelable

@Parcelize
data class PlaceDescription(

    @ColumnInfo
    val imagePath: String,      // 객실 원본 이미지

    @ColumnInfo
    val subject: String,        // 객실 설명

    @ColumnInfo
    val price: Int              // 숙박료

) : Parcelable