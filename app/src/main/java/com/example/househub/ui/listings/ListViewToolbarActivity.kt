package com.example.househub.ui.listings

import android.support.design.widget.NavigationView;
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.R
import com.example.househub.ui.account.AccountInfoActivity
import com.example.househub.ui.login.LoginActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

open class ListViewToolbarActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview_toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        val userId = intent.getIntExtra("userId", -1)
        val userId2 = LoginActivity.userIdGlobal

        findViewById<NavigationView>(R.id.navigation_view).setItemIconTintList(null)

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ){
            override fun onDrawerClosed(view:View){
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
        navigation_view.setNavigationItemSelectedListener{
            when (it.itemId){
                R.id.action_view_listings -> {
                }
                R.id.action_my_account -> {
                    val intent = Intent(this, AccountInfoActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            // Close the drawer
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        Thread(Runnable {
            val jwt = JWT()
            val payload = mapOf("requesterid" to LoginActivity.userIdGlobal)

            val url = "http://u747950311.hostingerapp.com/househub/api/listings/retrieve.php"
            //var success: Map<String, Any>? = null
            var success = ""
            var fail = ""

            val httpRequest = HTTPRequest(url, payload, success, fail)
            val results = httpRequest.open()

            success = results.first
            fail = results.second

            if(fail != "") {
                // TODO: handle failed retrieval
            }

            val retrievedInfo = jwt.decodePayloadSublet(success)

            //val user = LoggedInUser(retrievedInfo["fname"].toString(), retrievedInfo["lname"].toString(), retrievedInfo["fname"].toString() + " " + retrievedInfo["lname"], retrievedInfo["email"].toString(), retrievedInfo["admin"].toString(), retrievedInfo["created"].toString(), retrievedInfo["uid"].toString().toInt())

            val didItCrash = false

        }).start()

        // List View
        /*val listView = findViewById<ListView>(R.id.sublet_list_view)
        val subletList = Recipe.getRecipesFromFile("recipes.json", this)
        val listItems = arrayOfNulls<String>(subletList.size)
        for (i in 0 until subletList.size) {
            val recipe = subletList[i]
            listItems[i] = recipe.title
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter*/

    }



    fun setFieldValues(listing: Map<String, String>) {
        // TODO: Grab images map from listing["images"] (possibly need to convert another json string)
        // TODO: Add buttons to sides of imageview in order to be able to switch through the images array
        // set all field values!
    }
}