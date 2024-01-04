package cz.mendelu.pef.flashyflashcards.model

import androidx.annotation.StringRes
import cz.mendelu.pef.flashyflashcards.R

enum class BusinessCategory(
    @StringRes
    val displayName: Int,
    val alias: String
) {
    All(
        displayName = R.string.all_categories,
        alias = "language_schools,libraries,culturalcenter,museums,galleries"
    ),
    LanguageSchools(
        displayName = R.string.language_schools_category,
        alias = "language_schools"
    ),
    Libraries(
        displayName = R.string.libraries_category,
        alias = "libraries"
    ),
    CulturalCenter(
        displayName = R.string.cultural_centers_category,
        alias = "culturalcenter"
    ),
    Museums(
        displayName = R.string.museums_category,
        alias = "museums"
    ),
    ArtGalleries(
        displayName = R.string.art_galleries_category,
        alias = "galleries"
    )
}
