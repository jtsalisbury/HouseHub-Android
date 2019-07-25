package com.example.househub.ui.listings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.example.househub.R
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setSupportActionBar(findViewById(R.id.toolbar))
        /*val actionBar = supportActionBar
        actionBar?.title = ""*/

        findViewById<NavigationView>(R.id.navigation_view).setItemIconTintList(null);

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ){
            override fun onDrawerClosed(view: View){
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Set navigation view navigation item selected listener
        navigation_view.setNavigationItemSelectedListener {
            var selection = 0
            when (it.itemId) {
                R.id.action_view_listings -> {
                    toast("Cut clicked")
                    selection = 1
                }
                R.id.action_my_account -> {
                    toast("Copy clicked")
                    selection = 2
                }
                R.id.action_logout -> {
                    toast("Paste clicked")
                    selection = 3
                }
            }

            // Close the drawer
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }
}
