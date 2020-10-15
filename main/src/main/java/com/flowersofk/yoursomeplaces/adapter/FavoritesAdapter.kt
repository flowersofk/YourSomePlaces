package com.flowersofk.yoursomeplaces.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.flowersofk.yoursomeplaces.PlaceDetailActivity
import com.flowersofk.yoursomeplaces.R
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.utils.Constants.EXTRA_DATA_1
import com.flowersofk.yoursomeplaces.utils.Constants.EXTRA_DATA_2
import com.flowersofk.yoursomeplaces.utils.Constants.REGIST_DATE_FORMAT
import com.flowersofk.yoursomeplaces.utils.Utils
import com.flowersofk.yoursomeplaces.utils.Utils.convertLongToTime
import com.flowersofk.yoursomeplaces.utils.Utils.setGlide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_list_row_place.view.*

/**
 * 즐겨찾기 List Adapater
 */
class FavoritesAdapter(private val context: Context) :
    ListAdapter<PlaceProduct, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    fun setOnClickLikeListener(listner: OnClickLikeListener) {
        listener = listner
    }

    fun setOnItemClickLikeListener(listner: PlaceAdapter.Companion.OnItemClickLikeListener) {
        PlaceAdapter.onItemClickLikeListener = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoritesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as FavoritesViewHolder).bind(context, repoItem, position)
        }
    }

    companion object {

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<PlaceProduct>() {
            override fun areItemsTheSame(oldItem: PlaceProduct, newItem: PlaceProduct): Boolean =
                // UserInfo 데이터 중 고유한 값으로 비교
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PlaceProduct, newItem: PlaceProduct): Boolean =
                oldItem == newItem
        }

        // 좋아요 Callback Listener
        var listener: OnClickLikeListener? = null
        interface OnClickLikeListener { fun onClick(view: View, data: PlaceProduct) }

        var onItemClickLikeListener: OnItemClickLikeListener? = null
        interface OnItemClickLikeListener { fun onItemClick(view: View, data: PlaceProduct, isFavorite: Boolean) }

    }

    class FavoritesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(context: Context, place: PlaceProduct, pos: Int) {

            // 썸네일
            setGlide(context, place.thumbnail, containerView.iv_thumb, CenterCrop(),30)

            containerView.tv_siteName.text = place.name
            containerView.tv_rate.text = place.rate.toString()

            // 좋아요 버튼(애니메이션)
            var scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
            scaleAnimation.duration = 500
            scaleAnimation.interpolator = BounceInterpolator()

            containerView.tb_favorite.setOnCheckedChangeListener(null)
            containerView.tb_favorite.isChecked = true
            containerView.tb_favorite.setOnCheckedChangeListener { view, isFavorite ->
                view?.startAnimation(scaleAnimation)

                // ViewModel을 호출하기 위해 Fragment영역에서 Callback처리
                listener?.onClick(view, place)

            }

            containerView.setOnClickListener {
                PlaceAdapter.onItemClickLikeListener?.onItemClick(it, place, containerView.tb_favorite.isChecked)
            }

            place.timeStamp.let {
                containerView.tv_timeStamp.visibility = View.VISIBLE
                containerView.tv_timeStamp.text = convertLongToTime(it, REGIST_DATE_FORMAT)
            }

        }

        companion object {
            fun create(parent: ViewGroup): FavoritesViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_list_row_place, parent, false)
                return FavoritesViewHolder(view)
            }
        }

    }

}