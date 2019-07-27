package com.example.househub.ui.detailListing

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.R
import com.example.househub.ui.account.AccountInfoActivity
import com.example.househub.ui.listings.ListViewToolbarActivity
import com.example.househub.ui.login.LoginActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_list_view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

class DetailListView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list_view)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        val test = intent.getStringExtra("title")
//        val num_pictures = getIntent.getIntExtra()
//        val user_id = getIntent.getIntExtra()
        var picture_id = 0
        var url = "http://u747950311.hostingerapp.com/househub/api/images"

        findViewById<NavigationView>(R.id.detail_navigation_view).setItemIconTintList(null)

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            detail_drawer_layout,
            detail_toolbar,
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
        detail_drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Set navigation view navigation item selected listener
        detail_navigation_view.setNavigationItemSelectedListener{
            when (it.itemId){
                R.id.action_view_listings -> {
                    toast("Cut clicked")
                    val intent = Intent(this, ListViewToolbarActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_my_account -> {
                    toast("Copy clicked")
                    val intent = Intent(this, AccountInfoActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_logout -> {
                    toast("Paste clicked")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                /*R.id.action_new ->{
                    // Multiline action
                    toast("New clicked")
                }*/

            }
            // Close the drawer
            detail_drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

//        fun setPicture(left_right: Boolean) { //left = false, right = true
//            if(left_right == false && picture_id > 0){
//                //move to previous picture
//                picture_id -= 1
//                url = url + "/" + user_id.toString() + "/" + picture_id.toString() + ".jpg"
//                Picasso.get().load(url).into(images)
//            }
//            else if(left_right == true && picture_id < (num_pictures + 1)){
//                //move to next picture
//                picture_id += 1
//                url = url + "/" + user_id.toString() + "/" + picture_id.toString() + ".jpg"
//                Picasso.get().load(url).into(images)
//            }
//            else{
//                //do nothing
//            }
//        }
//
//        arrow_left.setOnClickListener{
//            setPicture(false)
//        }
//
//        arrow_right.setOnClickListener{
//            setPicture(true)
//        }


//        val num_pictures_text: TextView = findViewById<TextView>(R.id.desc)
//        num_pictures_text.text = "num_pictures = " + num_pictures.toString()
//
//        val user_id_text: TextView = findViewById<TextView>(R.id.price)
//        user_id_text.text = "user_id = " + user_id.toString()

        Picasso.get().load("http://u747950311.hostingerapp.com/househub/api/images/1/0.jpg").into(images)


    }

}
