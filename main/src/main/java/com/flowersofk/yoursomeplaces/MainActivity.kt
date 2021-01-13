package com.flowersofk.yoursomeplaces

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.flowersofk.yoursomeplaces.adapter.ViewPagerAdapter
import com.flowersofk.yoursomeplaces.viewmodel.PlaceViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 메인 ViewPagerActivity
 * commit test
 */
class MainActivity : AppCompatActivity() {

    private lateinit var placeViewModel: PlaceViewModel

    private var test = "BranchTest"; // 테스트용 코드
    private var test1 = "BranchTest"; // 테스트용 코드
    private var test2 = "BranchTest"; // 테스트용 코

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initVM()

        var map = HashMap<String, String>()
//        var set = map.keySet()
    }

    private fun initUI() {

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout!!, viewPager!!, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_search)
                1 -> tab.text = getString(R.string.tab_favorites)
            }
        }).attach()

    }

    private fun initVM() {

        var test = 123 // develop branch
        
        test.let{
            var result = it.plus(3)
            Log.i("test", "result : " + result)
        }

        ViewModelProvider(this).get(PlaceViewModel::class.java).requestPlaceList()

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


}