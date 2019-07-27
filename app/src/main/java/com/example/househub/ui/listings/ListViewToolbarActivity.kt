package com.example.househub.ui.listings

import android.content.Context
import android.support.design.widget.NavigationView
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.R
import com.example.househub.data.model.Listing
import com.example.househub.ui.account.AccountInfoActivity
import com.example.househub.ui.detailListing.DetailListView
import com.example.househub.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_listview_toolbar.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.drawer_layout
import kotlinx.android.synthetic.main.toolbar.navigation_view
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.coroutines.*

open class ListViewToolbarActivity : AppCompatActivity() {

    private lateinit var listings: List<Listing>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview_toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))

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

        val jwt = JWT()
        val payload = mapOf("requesterid" to LoginActivity.userIdGlobal)

        val url = "http://u747950311.hostingerapp.com/househub/api/listings/retrieve.php"
        //var success: Map<String, Any>? = null
        var success = ""
        var fail = ""

        val deferred = GlobalScope.async {
            val httpRequest = HTTPRequest(url, payload, success, fail)
            val results = httpRequest.open()

            success = results.first
            fail = results.second

            if(fail != "") {
                // TODO: handle failed retrieval
            }

            val retrievedInfo = jwt.decodePayloadSublet(success)
            listings = retrievedInfo.listings

        }

        runBlocking {
            while(!deferred.isCompleted) {
                delay(10)
            }
            val listView = findViewById<ListView>(R.id.sublet_list_view)
            val listItems = arrayOfNulls<String>(listings.size)
            for (i in 0 until listings.size) {
                val listing = listings[i]
                listItems[i] = listing.title
            }
            val adapter = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, listItems)
            listView.adapter = adapter
        }

        val context = this
        sublet_list_view.setOnItemClickListener { _, _, position, _ ->
            val selectedListing = listings[position]
            val intent = Intent(context, DetailListView::class.java)
            intent.putExtra("additionalPrice", selectedListing.add_price)
            intent.putExtra("basePrice", selectedListing.base_price)
            intent.putExtra("dateCreated", selectedListing.created)
            intent.putExtra("creatorEmail", selectedListing.creator_email)
            intent.putExtra("creatorFirstName", selectedListing.creator_fname)
            intent.putExtra("creatorLastName", selectedListing.creator_lname)
            intent.putExtra("creatorUserId", selectedListing.creator_uid)
            intent.putExtra("description", selectedListing.desc)
            intent.putExtra("isHidden", selectedListing.hidden)
            intent.putExtra("location", selectedListing.loc)
            intent.putExtra("dateModified", selectedListing.modified)
            intent.putExtra("numberOfPictures", selectedListing.num_pictures)
            intent.putExtra("propertyId", selectedListing.pid)
            intent.putExtra("title", selectedListing.title)
            startActivity(intent)
        }
    }
}