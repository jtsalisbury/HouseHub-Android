package com.example.househub.ui.listings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.househub.R
import android.content.Intent
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.view.View
import android.widget.ListView
import com.example.househub.ui.toolbar.ToolbarActivity


class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }

    /*fun onListItemClick(parent: ListView, v: View, position: Int, id: Long) {
        val bundle = Bundle()
        intent = Intent(baseContext, ToolbarActivity::class.java)
        bundle.putString("string", strings[position])
        intent.putExtras(bundle)
        startActivity(intent)
    }*/
}
