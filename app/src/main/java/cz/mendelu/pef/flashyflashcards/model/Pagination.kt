package cz.mendelu.pef.flashyflashcards.model

data class Pagination(
    var offset: Int = 0,
    var isEndOfPagination: Boolean = false
)
