package com.example.househub.data.model

data class Listing(
    val pid: String,
    val title: String,
    val desc: String,
    val loc: String,
    val base_price: String,
    val add_price: String,
    val creator_uid: String,
    val num_pictures: String,
    val created: String,
    val modified: String,
    val creator_fname: String,
    val creator_lname: String,
    val creator_email: String,
    val hidden: String,
    val images: List<String>
)

data class ListingPayload(
    val page: String,
    val total_pages: String,
    val listing_count: String,
    val max_listing_count: String,
    val listings: List<Listing>
)
