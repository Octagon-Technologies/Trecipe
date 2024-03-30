package com.octagontechnologies.trecipe.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val supportFragmentManager = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = supportFragmentManager.findNavController()
//        ViewUtils.setupStatusBarAndNavigationBar(this)

        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}