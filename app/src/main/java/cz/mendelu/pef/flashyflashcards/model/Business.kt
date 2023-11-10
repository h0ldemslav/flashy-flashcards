package cz.mendelu.pef.flashyflashcards.model

data class Business(
    var id: Long? = null,
    var remoteId: String,
    var name: String,
    var category: String,
    var displayAddress: List<String>,
    var businessUrl: String,
    var rating: String,
    var reviewCount: Int,
    var latitude: Double,
    var longitude: Double,
    var whenAdded: Long = 0L,
)
