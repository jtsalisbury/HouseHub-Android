package com.example.househub.ui.listings

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import com.example.househub.R
import com.example.househub.ui.toolbar.ToolbarActivity
import kotlinx.android.synthetic.main.activity_list_view.*





class ListViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)*/

        setContentView(R.layout.activity_list_view)

        // Find the toolbar view inside the activity layout
        //val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //setSupportActionBar(toolbar)

        /*val view = LayoutInflater.from(this).inflate(R.layout.toolbar)
        view.tvErrorTitle.text = "test"*/

        // Get the LayoutInflater from Context
        /*val layoutInflater:LayoutInflater = LayoutInflater.from(applicationContext)

        // Inflate the layout using LayoutInflater
        val view: View = layoutInflater.inflate(
            R.layout.toolbar, // Custom view/ layout
            , // Root layout to attach the view
            false // Attach with root layout or not
        )
*/

        //LayoutInflater.from(this).inflate(R.id., , false)

        //setSupportActionBar(toolbar)
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
    }
}
