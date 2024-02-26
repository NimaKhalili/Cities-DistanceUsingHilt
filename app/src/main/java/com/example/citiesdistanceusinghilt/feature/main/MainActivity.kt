package com.example.citiesdistanceusinghilt.feature.main

import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.citiesdistanceusinghilt.R
import com.example.citiesdistanceusinghilt.common.BaseActivity
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.databinding.ActivityMainBinding
import com.google.android.material.R.attr
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.color.MaterialColors
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarMain)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainer_main
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = binding.bottomNavigationViewMain
        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home, R.id.distance_List, R.id.gas)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDistanceItemCountChangeEvent(distanceItemCount: DistanceItemCount) {
        val badge = binding.bottomNavigationViewMain.getOrCreateBadge(R.id.distance_List)
        badge.badgeGravity = BadgeDrawable.BOTTOM_END
        badge.backgroundColor = MaterialColors.getColor(binding.bottomNavigationViewMain, attr.colorPrimary)
        badge.number = distanceItemCount.count
        badge.isVisible = distanceItemCount.count > 0
    }

    /*override fun onBackPressed() {
        super.onBackPressedDispatcher()
        finish()
    }*/

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        finish()
        return super.getOnBackInvokedDispatcher()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDistanceItemCount()
    }
}