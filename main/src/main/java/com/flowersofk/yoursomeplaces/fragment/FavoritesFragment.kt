package com.flowersofk.yoursomeplaces.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flowersofk.yoursomeplaces.PlaceDetailActivity
import com.flowersofk.yoursomeplaces.R
import com.flowersofk.yoursomeplaces.adapter.FavoritesAdapter
import com.flowersofk.yoursomeplaces.adapter.PlaceAdapter
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.utils.Constants
import com.flowersofk.yoursomeplaces.viewmodel.PlaceFavoriteViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_search.fab_top
import kotlinx.android.synthetic.main.fragment_search.rv_userList
import kotlinx.android.synthetic.main.view_sort_dialog.*
import kotlinx.android.synthetic.main.view_sort_dialog.view.*

/**
 * 즐겨찾기 화면
 */
class FavoritesFragment : BaseFragment() {

    private val REQUEST_DETAIL = 0

    private lateinit var placeFavoriteViewModel: PlaceFavoriteViewModel
    private lateinit var mAdapter: FavoritesAdapter

    // 정렬 타입 ID
    private var sortId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initVM()

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        // 변경된 데이터 UI 갱신
        if(menuVisible) {

            var list = sortList(sortId)
            if(list.isEmpty()) {
                ll_empty.visibility = View.VISIBLE
            } else {
                ll_empty.visibility = View.GONE
                placeFavoriteViewModel.getFavoritesPlace(list)
            }

        }

    }

    private fun initUI() {

        mAdapter = FavoritesAdapter(requireContext())
        mAdapter.setOnClickLikeListener(object: FavoritesAdapter.Companion.OnClickLikeListener {
            override fun onClick(view: View, place: PlaceProduct) {

                // 즐겨찾기 해제
                dbHelper.placeFavoriteDao().delete(place)

                var list = sortList(sortId)
                if(list.isEmpty()) {
                    ll_empty.visibility = View.VISIBLE
                } else {
                    ll_empty.visibility = View.GONE
                    placeFavoriteViewModel.getFavoritesPlace(list)
                }
            }
        })

        mAdapter.setOnItemClickLikeListener(object : PlaceAdapter.Companion.OnItemClickLikeListener {
            override fun onItemClick(view: View, data: PlaceProduct, isFavorite: Boolean) {
                val intent = Intent(context, PlaceDetailActivity::class.java)
                intent.putExtra(Constants.EXTRA_DATA_1, data)
                intent.putExtra(Constants.EXTRA_DATA_2, isFavorite)
                requireActivity().startActivityForResult(intent, REQUEST_DETAIL)
            }

        })

        with(rv_userList) {

            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = mAdapter

        }

        // 즐겨찾기 최근등록순, 평점순 정렬 기능 구현 (오름차순, 내림차순)
        fab_sorting.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity())
            val dialogView = layoutInflater.inflate(R.layout.view_sort_dialog, null)

            builder.setView(dialogView)
            var alertDialog = builder.show()

            // 현재 적용 중인 정렬 check true
            when(sortId) {
                // 최근등록순
                R.id.rb_sort_recency -> dialogView.rb_sort_recency.isChecked = true
                // 높은평점순 (내림차순)
                R.id.rb_sort_rate_high -> dialogView.rb_sort_rate_high.isChecked = true
                // 낮은평점순 (오름차순)
                R.id.rb_sort_rate_low -> dialogView.rb_sort_rate_low.isChecked = true
                // 기본값(최근등록순)
                else -> dialogView.rb_sort_recency.isChecked = true

            }

            // 정렬 적용
            dialogView.btn_sort.setOnClickListener {

                sortId = dialogView.rg_sort.checkedRadioButtonId

                // 정렬
                placeFavoriteViewModel.getFavoritesPlace(sortList(sortId))
                alertDialog.dismiss()
            }

            // 취소
            dialogView.btn_cancel.setOnClickListener {
                alertDialog.dismiss()
            }

        }

    }

    private fun initVM() {

        placeFavoriteViewModel = ViewModelProvider(requireActivity()).get(PlaceFavoriteViewModel::class.java)
        placeFavoriteViewModel.favoritesLiveData.observe(requireActivity(), Observer<List<PlaceProduct>> {
            mAdapter.submitList(it)
        })
    }

    private fun sortList(sortId: Int): List<PlaceProduct> {

        var list: List<PlaceProduct>

        when(sortId) {
            // 최근등록순
            R.id.rb_sort_recency -> {
                list = dbHelper.placeFavoriteDao().getDateSortFavorites()
            }
            // 높은평점순 (내림차순)
            R.id.rb_sort_rate_high -> {
                list = dbHelper.placeFavoriteDao().getRateSortFavorites(0)
            }
            // 낮은평점순 (오름차순)
            R.id.rb_sort_rate_low -> {
                list = dbHelper.placeFavoriteDao().getRateSortFavorites(1)
            }
            // 기본값(최근등록순)
            else -> {
                list = dbHelper.placeFavoriteDao().getDateSortFavorites()
            }

        }

        return list

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {
            REQUEST_DETAIL -> {
                // 정렬
                placeFavoriteViewModel.getFavoritesPlace(sortList(sortId))
            }
        }
    }

}