package com.example.househub.ui.listings

import android.content.Context
import android.support.design.widget.NavigationView
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.R
import com.example.househub.data.model.Listing
import com.example.househub.ui.listings.*
import com.example.househub.ui.account.AccountInfoActivity
import com.example.househub.ui.detailListing.DetailListView
import com.example.househub.ui.login.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_listview_toolbar.*
import kotlinx.android.synthetic.main.toolbar.drawer_layout
import kotlinx.android.synthetic.main.toolbar.navigation_view
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.coroutines.*

open class ListViewToolbarActivity : AppCompatActivity() {

    private lateinit var listings: List<Listing>
    private var priceDescending = true
    private var dateAscending = true

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

        getListings()

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


        // Create Listeners
        search_listings_button.setOnClickListener {
            getListings()
            pricingArrow.setImageResource(0)
            dateArrow.setImageResource(0)
            priceDescending = true
            dateAscending = true
        }

        clear_button.setOnClickListener {
            searchListings.text.clear()
            minPriceText.text.clear()
            maxPriceText.text.clear()
            my_listings_button.isChecked = false
            saved_listings_button.isChecked = false
            pricingArrow.setImageResource(0)
            dateArrow.setImageResource(0)
            priceDescending = true
            dateAscending = true
        }

        pricingArrow.bringToFront()

        pricing_sort_button.setOnClickListener {
            dateArrow.setImageResource(0)
            dateAscending = true

            val sortedList = listings.sortedWith(compareBy{ it.base_price })
            if(priceDescending) {
                listings = sortedList.reversed()
                priceDescending = false
                pricingArrow.setImageResource(R.drawable.downarrow)

            }
            else {
                listings = sortedList
                priceDescending = true
                pricingArrow.setImageResource(R.drawable.uparrow)
            }

            val listView = findViewById<ListView>(R.id.sublet_list_view)
            val adapter = ListingAdapter(baseContext, listings)
            listView.adapter = adapter
        }

        created_date_sort_button.setOnClickListener {
            pricingArrow.setImageResource(0)
            priceDescending = true

            val sortedList = listings.sortedWith(compareBy{ it.created })
            if(dateAscending) {
                listings = sortedList.reversed()
                dateAscending = false
                dateArrow.setImageResource(R.drawable.downarrow)
            }
            else {
                listings = sortedList
                dateAscending = true
                dateArrow.setImageResource(R.drawable.uparrow)
            }

            val listView = findViewById<ListView>(R.id.sublet_list_view)
            val adapter = ListingAdapter(baseContext, listings)
            listView.adapter = adapter
        }
    }

    private fun getListings() {

        val jwt = JWT()
        //var payload = mapOf("requesterid" to LoginActivity.userIdGlobal) as MutableMap
        //if(my_listings_button.isChecked) payload.put("uid" to LoginActivity.userIdGlobal)
        val mutablePayload =
            mapOf("uid" to LoginActivity.userIdGlobal.toString(),
            "saved" to saved_listings_button.isChecked.toString(),
            "price_min" to minPriceText.text.toString(),
            "price_max" to maxPriceText.text.toString(),
            "search_criteria" to searchListings.text.toString(),
            "requesterid" to LoginActivity.userIdGlobal.toString()) as MutableMap

        if(!my_listings_button.isChecked && !saved_listings_button.isChecked) mutablePayload.remove("uid")
        if(!saved_listings_button.isChecked) mutablePayload.remove("saved")
        if(minPriceText.text.isNullOrEmpty()) mutablePayload.remove("price_min")
        if(maxPriceText.text.isNullOrEmpty()) mutablePayload.remove("price_max")
        if(searchListings.text.isNullOrEmpty()) mutablePayload.remove("search_criteria")

        val payload = mutablePayload as Map<String, Any>

        val url = "http://u747950311.hostingerapp.com/househub/api/listings/retrieve.php"
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
            val adapter = ListingAdapter(baseContext, listings)
            listView.adapter = adapter
        }
    }

    class ListingAdapter(context: Context, dataSource: List<Listing>) : BaseAdapter() {

        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private val listings = dataSource

        //1
        override fun getCount(): Int {
            return listings.size
        }

        //2
        override fun getItem(position: Int): Any {
            return listings[position]
        }

        //3
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        //4
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            // Get view for row item
            val rowView = inflater.inflate(R.layout.listview_listing_item, parent, false)

            // Get thumbnail element
            val thumbnailImageView = rowView.findViewById(R.id.listingImage) as ImageView

            // Get title element
            val titleTextView = rowView.findViewById(R.id.listingTitle) as TextView

            // Get subtitle element
            val locationTextView = rowView.findViewById(R.id.listingLocation) as TextView

            // Get subtitle element
            val priceTextView = rowView.findViewById(R.id.listingPrice) as TextView

            // Get detail element
            val dateCreatedTextView = rowView.findViewById(R.id.listingDateCreated) as TextView

            // 1
            val listing = getItem(position) as Listing

            val url = "http://u747950311.hostingerapp.com/househub/api/images"
            Picasso.get().load(url + "/" + listing.pid + "/0.jpg").into(thumbnailImageView)

            titleTextView.text = listing.title
            locationTextView.text = listing.loc
            val price = "$" + listing.base_price
            priceTextView.text = price
            dateCreatedTextView.text = listing.created

            return rowView
        }
    }
}