package cz.mendelu.pef.flashyflashcards.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.NavGraphSpec
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs

@Composable
fun DestinationsNavHostWrapper(
    navGraph: NavGraphSpec = NavGraphs.root,
    navController: NavHostController = rememberNavController()
) {
    DestinationsNavHost(
        navGraph = navGraph,
        navController = navController
    )
}