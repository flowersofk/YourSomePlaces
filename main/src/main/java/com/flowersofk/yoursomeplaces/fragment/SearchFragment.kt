package com.flowersofk.yoursomeplaces.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.flowersofk.yoursomeplaces.PlaceDetailActivity
import com.flowersofk.yoursomeplaces.R
import com.flowersofk.yoursomeplaces.adapter.PlaceAdapter
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.utils.Constants
import com.flowersofk.yoursomeplaces.utils.Utils
import com.flowersofk.yoursomeplaces.viewmodel.PlaceViewModel
import kotlinx.android.synthetic.main.fragment_search.*


/**
 * 메인결과 화면
 */
class SearchFragment : BaseFragment() {

    private val REQUEST_DETAIL = 0

    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var mAdapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
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
            mAdapter.favorites = dbHelper.placeFavoriteDao().getAllFavorites()
            mAdapter.notifyDataSetChanged()
        }

    }

    private fun initVM() {
        placeViewModel = ViewModelProvider(requireActivity()).get(PlaceViewModel::class.java)
        placeViewModel.resultList.observe(requireActivity(), Observer<PagedList<PlaceProduct>> {
            mAdapter.submitList(it)
        })
    }

    private fun initUI() {

        mAdapter = PlaceAdapter(requireContext())

        // 즐겨찾기 목록
        mAdapter.favorites = dbHelper.placeFavoriteDao().getAllFavorites()
        mAdapter.setOnClickLikeListener(object: PlaceAdapter.Companion.OnClickLikeListener {
            override fun onClick(view: View, place: PlaceProduct) {

                if(dbHelper.placeFavoriteDao().getSelectPlace(place.id) != null) {
                    dbHelper.placeFavoriteDao().delete(place)
                } else {
                    dbHelper.placeFavoriteDao().insertWithTimestamp(place)

                    Utils.snackBar(getString(R.string.favorite_add, place.name), getString(R.string.undo),view, View.OnClickListener {
                        dbHelper.placeFavoriteDao().delete(place)
                        mAdapter.notifyDataSetChanged()
                    })
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

        // 최상단 이동 FAB
        fab_top.setOnClickListener {
            rv_userList.scrollToPosition(0)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {
            REQUEST_DETAIL -> {
                mAdapter.favorites = dbHelper.placeFavoriteDao().getAllFavorites()
                mAdapter.notifyDataSetChanged()
            }
        }
    }

}