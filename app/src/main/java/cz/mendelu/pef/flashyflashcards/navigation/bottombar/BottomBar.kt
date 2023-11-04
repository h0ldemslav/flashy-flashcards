package cz.mendelu.pef.flashyflashcards.navigation.bottombar

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
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

    BottomNavigation {
        BottomBarDestination.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination?.route == destination.graph.route,
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
                        contentDescription = stringResource(id = destination.label)
                    )
                },
                label = {
                    Text(text = stringResource(id = destination.label))
                }
            )
        }
    }
}