package com.example.instaclonekotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.adapter.ViewPagerAdapter
import com.example.instaclonekotlin.databinding.ActivityMainBinding
import com.example.instaclonekotlin.fragment.*

/**
 * It contains view pager with 5 fragments in MainActivity
 * and pages can controlled by BottomNavigation
 */

class MainActivity : BaseActivity(),UploadFragment.UploadListener,HomeFragment.HomeListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter by lazy { ViewPagerAdapter(supportFragmentManager) }
    var index = 0
    private val TAG = MainActivity::class.java.simpleName
    private val homeFragment by lazy { HomeFragment() }
    private val uploadFragment by lazy { UploadFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        setUpViewPager()

        binding.apply {
            bottomNav.setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.navigation_home -> viewPager.currentItem = 0
                    R.id.navigation_search -> viewPager.currentItem = 1
                    R.id.navigation_upload -> viewPager.currentItem = 2
                    R.id.navigation_favorite -> viewPager.currentItem = 3
                    R.id.navigation_profile -> viewPager.currentItem = 4
                }
                true
            }

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    index = position
                    bottomNav.menu.getItem(index).isChecked = true
                }

                override fun onPageScrollStateChanged(state: Int) {}

            })
        }
    }

    override fun scrollToHome() {
        index = 0
        scrollByIndex(index)
    }

    override fun scrollToUpload() {
        index = 2
        scrollByIndex(index)
    }

    private fun scrollByIndex(index: Int) {
        binding.apply {
            viewPager.currentItem = index
            bottomNav.menu.getItem(index).isChecked = true
        }
    }

    private fun setUpViewPager() {
        adapter.addFragment(homeFragment)
        adapter.addFragment(SearchFragment())
        adapter.addFragment(uploadFragment)
        adapter.addFragment(FavoriteFragment())
        adapter.addFragment(ProfileFragment())
        binding.viewPager.adapter = adapter
    }
}