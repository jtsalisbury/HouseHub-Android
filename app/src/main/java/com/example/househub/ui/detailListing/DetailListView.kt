package com.example.househub.ui.detailListing

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.R

import kotlinx.android.synthetic.main.activity_detail_list_view.*

abstract class DetailListView : AppCompatActivity() {

    abstract var listing: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list_view)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pid = getIntent().getIntExtra("pid", 0)
        val uid = getIntent().getIntExtra("uid", 0)

        val jwt = JWT()
        val payload = mapOf("pid" to pid, "requesterid" to uid)

        val req = HTTPRequest("http://u747950311.hostingerapp.com/househub/api/listings/retrieve.php", payload, "", "")
        val results = req.open()

        val success = results.first
        val fail    = results.second

        if (fail != "" || !jwt.verifyToken(success))
            return

        val data = jwt.decodePayload(success)


    }

}
