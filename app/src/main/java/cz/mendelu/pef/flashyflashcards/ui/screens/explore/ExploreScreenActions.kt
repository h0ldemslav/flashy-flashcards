package cz.mendelu.pef.flashyflashcards.ui.screens.explore

interface ExploreScreenActions {

    fun updateScreenData(data: ExploreScreenData)
    fun getBusinesses(locationName: String, categories: String)
}