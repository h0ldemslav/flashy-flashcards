package cz.mendelu.pef.flashyflashcards.model

import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity

data class Business(
    var id: Long? = null,
    var remoteId: String,
    var name: String,
    var imageUrl: String,
    var category: String,
    var displayAddress: List<String>,
    var businessUrl: String,
    var rating: String,
    var reviewCount: Int,
    var latitude: Double,
    var longitude: Double,
    var whenAdded: Long? = null
) {
    companion object {

        fun createFromBusinessEntity(businessEntity: BusinessEntity): Business {
            val displayAddress = businessEntity.displayAddress.split(",")

            return Business(
                id = businessEntity.id,
                remoteId = businessEntity.remoteId,
                name = businessEntity.name,
                imageUrl = businessEntity.imageUrl,
                category = businessEntity.category,
                displayAddress = displayAddress,
                businessUrl = businessEntity.businessUrl,
                rating = businessEntity.rating,
                reviewCount = businessEntity.reviewCount,
                latitude = businessEntity.latitude,
                longitude = businessEntity.longitude,
                whenAdded = businessEntity.whenAdded
            )
        }
    }
}
