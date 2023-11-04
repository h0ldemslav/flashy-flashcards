package cz.mendelu.pef.flashyflashcards.navigation.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.NavGraphSpec
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs

enum class BottomBarDestination(
    val graph: NavGraphSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Explore(NavGraphs.explore, Icons.Default.TravelExplore, R.string.explore),
    Collections(NavGraphs.collections, Icons.Default.StickyNote2, R.string.collections),
    Settings(NavGraphs.settings, Icons.Default.Settings, R.string.settings)
}