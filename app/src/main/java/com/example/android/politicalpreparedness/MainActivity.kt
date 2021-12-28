package com.example.android.politicalpreparedness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //add the navigation up within the app
       // val navController = this.findNavController(R.id.nav_host_fragment)
        //NavigationUI.setupActionBarWithNavController(this, navController)
        // # we are using FragmentContainerView, so need to revert to using findFragmentById
        val controller = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, controller.navController)

    }

    override fun onSupportNavigateUp(): Boolean {
       // val controller = this.findNavController(R.id.nav_host_fragment)
        //return controller.navigateUp()
        val controller = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return controller.navController.navigateUp()

    }
}
