package com.flowersofk.yoursomeplaces.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.flowersofk.yoursomeplaces.R
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.utils.Utils.setGlide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_list_row_place.view.*

class PlaceAdapter(private val context: Context)
    : PagedListAdapter<PlaceProduct, RecyclerView.ViewHolder> (REPO_COMPARATOR) {

    var favorites : List<PlaceProduct> = mutableListOf()

    fun setOnClickLikeListener(listner: OnClickLikeListener) {
        onClickLikeListener = listner
    }

    fun setOnItemClickLikeListener(listner: OnItemClickLikeListener) {
        onItemClickLikeListener = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaceViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as PlaceViewHolder).bind(context, repoItem, position, favorites)
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
        var onClickLikeListener: OnClickLikeListener? = null
        interface OnClickLikeListener { fun onClick(view: View, data: PlaceProduct) }

        var onItemClickLikeListener: OnItemClickLikeListener? = null
        interface OnItemClickLikeListener { fun onItemClick(view: View, data: PlaceProduct, isFavorite: Boolean) }

    }

    class PlaceViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(context: Context, place: PlaceProduct, pos: Int, favorites: List<PlaceProduct>) {

            // 썸네일
            setGlide(context, place.thumbnail, containerView.iv_thumb, CenterCrop(), 30)

            containerView.tv_siteName.text = place.name
            containerView.tv_rate.text = place.rate.toString()

            // 좋아요 버튼(애니메이션)
            var scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
            scaleAnimation.duration = 500
            scaleAnimation.interpolator = BounceInterpolator()

            containerView.tb_favorite.setOnCheckedChangeListener(null)
            containerView.tb_favorite.isChecked = favorites.any { it.id == place.id }
            containerView.tb_favorite.setOnCheckedChangeListener { view, isFavorite ->
                view?.startAnimation(scaleAnimation)

                // ViewModel을 호출하기 위해 Fragment영역에서 Callback처리
                onClickLikeListener?.onClick(view, place)
            }

            containerView.setOnClickListener {
                onItemClickLikeListener?.onItemClick(it, place, containerView.tb_favorite.isChecked)
            }

        }

        companion object {
            fun create(parent: ViewGroup): PlaceViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_list_row_place, parent, false)
                return PlaceViewHolder(view)
            }
        }

    }

}