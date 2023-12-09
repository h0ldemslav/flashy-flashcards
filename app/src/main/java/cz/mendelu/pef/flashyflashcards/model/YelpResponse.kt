package cz.mendelu.pef.flashyflashcards.model

import com.google.gson.annotations.SerializedName

data class YelpResponse(
    val businesses: List<BusinessDTO>,
    val total: Int,
    val region: Region
)

data class BusinessDTO(
    val id: String,
    val alias: String?,
    val name: String?,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("is_closed")
    val isClosed: Boolean?,
    val url: String? = null,
    @SerializedName("review_count")
    val reviewCount: Int? = null,
    val categories: List<Category>? = null,
    val rating: String? = null,
    val coordinates: Coordinate?,
    val transactions: List<String>? = null,
    val price: String? = null,
    val location: Location?,
    val phone: String?,
    @SerializedName("display_phone")
    val displayPhone: String?,
    val distance: String? = null,
    val hours: List<BusinessHours>?
)

data class Category(
    val alias: String?,
    val title: String?
)

data class Coordinate(
    val latitude: String?,
    val longitude: String?
)

data class Location(
    val address1: String? = null,
    val address2: String? = null,
    val address3: String? = null,
    val city: String? = null,
    @SerializedName("zip_code")
    val zipCode: String? = null,
    val country: String? = null,
    val state: String? = null,
    @SerializedName("display_address")
    val displayAddress: List<String>?,
    @SerializedName("cross_streets")
    val crossStreets: String?
)

data class BusinessHours(
    @SerializedName("hour_type")
    val hourType: String?,
    val open: List<OpenHours>?,
    @SerializedName("is_open_now")
    val isOpenNow: Boolean?
)

data class OpenHours(
    val day: Int?,
    val start: String?,
    val end: String?,
    @SerializedName("is_overnight")
    val isOvernight: Boolean?,
)

data class Region(
    val center: Location?
)

