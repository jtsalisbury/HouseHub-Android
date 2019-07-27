package com.example.househub.ui.listings

import android.media.Image

/**
 * Data class that captures listing information
 */
data class ListingData(
    val propertyId: Int,
    val title: String,
    val description: String,
    val location: String,
    val basePrice: Int,
    val additionalPrice: Int,
    val creatorUserId: Int,
    val numberPictures: Int,
    val createdDate: String,
    val modifiedDate: String,
    val creatorFirstName: String,
    val creatorLastName: String,
    val creatorEmail: String,
    val isHidden: Boolean,
    val isSaved: Boolean,
    val images: List<Image>

)


