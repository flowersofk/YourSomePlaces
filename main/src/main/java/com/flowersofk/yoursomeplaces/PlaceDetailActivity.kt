package com.flowersofk.yoursomeplaces

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.db.helper.PlaceFavoriteDbHelper
import com.flowersofk.yoursomeplaces.utils.Constants
import com.flowersofk.yoursomeplaces.utils.Constants.EXTRA_DATA_1
import com.flowersofk.yoursomeplaces.utils.Constants.EXTRA_DATA_2
import com.flowersofk.yoursomeplaces.utils.Utils
import com.flowersofk.yoursomeplaces.utils.Utils.getPriceFormat
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.view_list_row_place.view.*

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var placeInfo: PlaceProduct
    lateinit var dbHelper: PlaceFavoriteDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        dbHelper = Room.databaseBuilder(this, PlaceFavoriteDbHelper::class.java, "room_place")
            .allowMainThreadQueries()
            .build()

        initUI()

    }

    fun initUI() {

        if(intent.hasExtra(EXTRA_DATA_1)) {

            placeInfo = intent.getParcelableExtra<PlaceProduct>(EXTRA_DATA_1)

            Glide.with(this).load(placeInfo.description.imagePath).into(iv_image)   // 썸네일

            iv_image.setOnClickListener {
                val intent = Intent(this, ImageBrowerActivity::class.java)
                intent.putExtra(EXTRA_DATA_1, placeInfo.description.imagePath)
                startActivity(intent)
            }

            tv_name.text = placeInfo.name
            tv_subject.text = placeInfo.description.subject
            tv_price.text = getString(R.string.won_format, getPriceFormat(placeInfo.description.price))
            tv_rate.text = placeInfo.rate.toString()

            if(intent.hasExtra(EXTRA_DATA_2)) {
                tb_favorite.isChecked = intent.getBooleanExtra(EXTRA_DATA_2, false)
            }

        } else {

            finish()

        }

        btn_back.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        // 좋아요 버튼(애니메이션)
        var scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        scaleAnimation.interpolator = BounceInterpolator()

        tb_favorite.setOnCheckedChangeListener { view, isFavorite ->
            view?.startAnimation(scaleAnimation)

            if(dbHelper.placeFavoriteDao().getSelectPlace(placeInfo.id) != null) {
                dbHelper.placeFavoriteDao().delete(placeInfo)
            } else {
                dbHelper.placeFavoriteDao().insertWithTimestamp(placeInfo)

                Utils.snackBar(getString(R.string.favorite_add, placeInfo.name), getString(R.string.undo),view, View.OnClickListener {
                    dbHelper.placeFavoriteDao().delete(placeInfo)
                })
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        setResult(Activity.RESULT_OK)
        finish()

    }
}