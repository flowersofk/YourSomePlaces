package com.flowersofk.yoursomeplaces.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.flowersofk.yoursomeplaces.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.view_list_row_place.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun snackBar(msg: String?, actionMsg: String?, v: View, listener: View.OnClickListener) {
        Snackbar.make(v, msg!!, Snackbar.LENGTH_LONG).setAction(actionMsg, listener).show()
    }

    /**
     * Long -> Date String
     */
    fun convertLongToTime(time: Long, format: String): String {
        val date = Date(time)
        val format = SimpleDateFormat(format)
        return format.format(date)
    }

    /**
     * 가격포맷 변환
     * @return 123,456
     */
    fun getPriceFormat(cost: Int): String {
        return DecimalFormat("###,###").format(cost)
    }

    fun setGlide(context: Context, url: String, view: ImageView, scaleType: BitmapTransformation, radius: Int) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_defualt_image)
            .error(R.drawable.ic_error_image)
            .transform(scaleType, RoundedCorners(radius))
            .into(view)
    }

}
