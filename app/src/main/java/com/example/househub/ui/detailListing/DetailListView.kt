package com.example.househub.ui.detailListing

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail_list_view.toolbar
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

class DetailListView : AppCompatActivity() {

    fun setFieldValues(listing: Map<String, String>) {
        // TODO: Grab images map from listing["images"] (possibly need to convert another json string)
        // set all field values!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview_toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        //val userId = intent.getIntExtra("userId", -1)

        findViewById<NavigationView>(R.id.navigation_view).setItemIconTintList(null)

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
        navigation_view.setNavigationItemSelectedListener{
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
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        val pid = getIntent().getIntExtra("pid", 0)
        val uid = getIntent().getIntExtra("uid", 0)

        val jwt = JWT()
        val payload = mapOf("pid" to pid, "requesterid" to uid)

        Thread(Runnable {
            val req = HTTPRequest("http://u747950311.hostingerapp.com/househub/api/listings/retrieve.php", payload, "", "")
            val results = req.open()

            val success = results.first
            val fail    = results.second
            val gson = Gson()

            if (fail == "" && jwt.verifyToken(success)) {
                var payload = jwt.decodePayload(success)

                /*

                    THIS LINE MAY BE WRONG. LISTINGS MAY BE RETURNED AS A MAP, NOT A STRING

                 */
                val m = object : TypeToken<Map<String, String>>() {}.type
                payload = gson.fromJson(payload["listings"].toString(), m)

                // get first listing from payload map
                var first = payload[payload.keys.toTypedArray()[0]].toString()

                var listing: Map<String, String> = gson.fromJson(first, m)

                setFieldValues(listing)
            }

        }).start()

    }

}
