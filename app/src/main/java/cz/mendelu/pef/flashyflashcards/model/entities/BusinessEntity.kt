package cz.mendelu.pef.flashyflashcards.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.pef.flashyflashcards.model.Business

@Entity(tableName = "businesses")
data class BusinessEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var remoteId: String,
    var name: String,
    var imageUrl: String,
    var category: String,
    var displayAddress: String,
    var businessUrl: String,
    var rating: String,
    var reviewCount: Int,
    var latitude: Double,
    var longitude: Double,
    var whenAdded: Long
) {
    companion object {

        fun createFromBusiness(business: Business): BusinessEntity {
            val displayAddress = business.displayAddress.joinToString()

            return BusinessEntity(
                id = business.id,
                remoteId = business.remoteId,
                name = business.name,
                imageUrl = business.imageUrl,
                category = business.category,
                displayAddress = displayAddress,
                businessUrl = business.businessUrl,
                rating = business.rating,
                reviewCount = business.reviewCount,
                latitude = business.latitude,
                longitude = business.longitude,
                whenAdded = business.whenAdded ?: 0L
            )
        }
    }
}
