package com.example.senamit.stationarynavigationtwo.Canary

import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar

import com.example.senamit.stationarynavigationtwo.R

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class CanaryMainActivity : AppCompatActivity() {

    private var drawerLayout: DrawerLayout?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canary_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
//        supportActionBar?.run { setDisplayHomeAsUpEnabled(true)
//        setHomeButtonEnabled(true)}

        val host: NavHostFragment =
                supportFragmentManager
                        .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        setupActionBar(navController)

    }
    private fun setupActionBar(navController: NavController) {
        drawerLayout = findViewById(R.id.drawer_layout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(drawerLayout,
                Navigation.findNavController(this, R.id.my_nav_host_fragment))
    }



}

//    private var drawerLayout: DrawerLayout? = null
//    private var toolbar: Toolbar? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_canary_main)
//
//        //        if (savedInstanceState==null){
//        //            getSupportFragmentManager().beginTransaction().replace(R.id.)
//        //        }
//
//        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        val host = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
//
//        val navController = host.navController
//
//        setUpActionBar(navController)
//
//    }
//
//    private fun setUpActionBar(navController: NavController) {
//        drawerLayout = findViewById(R.id.drawer_layout)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(drawerLayout,
//                Navigation.findNavController(this, R.id.my_nav_host_fragment))
//
//    }

