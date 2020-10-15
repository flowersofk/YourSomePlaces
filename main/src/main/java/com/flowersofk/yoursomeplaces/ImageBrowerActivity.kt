package com.flowersofk.yoursomeplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.flowersofk.yoursomeplaces.utils.Constants
import com.flowersofk.yoursomeplaces.utils.Constants.EXTRA_DATA_1
import kotlinx.android.synthetic.main.activity_place_detail.*

/**
 * 이미지 뷰어
 */
class ImageBrowerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_brower)

        initUI()

    }

    private fun initUI() {

        if(intent.hasExtra(EXTRA_DATA_1)) {

            Glide.with(this).load(intent.getStringExtra(EXTRA_DATA_1)).into(iv_image)   // 썸네일

        }

        btn_back.setOnClickListener {
            finish()
        }

    }


}