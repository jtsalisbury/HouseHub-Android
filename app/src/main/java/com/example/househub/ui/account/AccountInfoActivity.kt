package com.example.househub.ui.account

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.TextView
import com.example.househub.R
import com.example.househub.data.model.LoggedInUser
import kotlinx.android.synthetic.main.activity_account_info.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

class AccountInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)
        setSupportActionBar(account_toolbar)

        setSupportActionBar(findViewById(R.id.account_toolbar))
        /*val actionBar = supportActionBar
        actionBar?.title = ""*/

        findViewById<NavigationView>(R.id.navigation_view).setItemIconTintList(null);

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            account_drawer_layout,
            account_toolbar,
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
        account_navigation_view.setNavigationItemSelectedListener{
            when (it.itemId){
                R.id.action_view_listings -> toast("Cut clicked")
                R.id.action_my_account -> toast("Copy clicked")
                R.id.action_logout -> toast("Paste clicked")
                /*R.id.action_new ->{
                    // Multiline action
                    toast("New clicked")
                }*/

            }
            // Close the drawer
            account_drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        // Fill in account text fields

        val account_name: TextView = findViewById<TextView>(R.id.account_name)
        val account_email: TextView = findViewById<TextView>(R.id.account_email)

//        account_name.text =
//        account_email.text =
//            account_date.text =
    }

}
