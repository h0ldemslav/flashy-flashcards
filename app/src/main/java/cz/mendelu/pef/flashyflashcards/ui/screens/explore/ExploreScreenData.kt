package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import cz.mendelu.pef.flashyflashcards.model.BusinessCategory

data class ExploreScreenData(
    var name: String = "",
    var businessCategory: BusinessCategory = BusinessCategory.All,
    var isValid: Boolean? = null
)