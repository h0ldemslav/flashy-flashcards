package cz.mendelu.pef.flashyflashcards.architecture

data class CommunicationError(
    val code: Int,
    var message: String?,
    var secondaryMessage: String? = null
)

