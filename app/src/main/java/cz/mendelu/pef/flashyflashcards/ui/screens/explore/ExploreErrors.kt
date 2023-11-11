package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ExploreErrors(
    @DrawableRes val imageRes: Int? = null,
    @StringRes val messageRes: Int
)
