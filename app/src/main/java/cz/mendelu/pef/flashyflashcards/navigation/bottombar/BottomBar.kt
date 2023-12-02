package cz.mendelu.pef.flashyflashcards.navigation.bottombar

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.utils.contains
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs
import cz.mendelu.pef.flashyflashcards.ui.screens.appCurrentDestinationAsState
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.Destination
import cz.mendelu.pef.flashyflashcards.ui.screens.startAppDestination

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination: Destination? =
        navController.appCurrentDestinationAsState().value ?: NavGraphs.root.startAppDestination

    BottomNavigation(backgroundColor = MaterialTheme.colorScheme.surface) {
        BottomBarDestination.values().forEach { destination ->
            val selected = currentDestination?.let { destination.graph.contains(it) } ?: false

            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.graph.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        tint = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(id = destination.label)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.label),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}