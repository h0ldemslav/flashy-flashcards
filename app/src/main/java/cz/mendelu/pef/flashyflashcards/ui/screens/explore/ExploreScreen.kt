package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.ExploreNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold

@ExploreNavGraph(start = true)
@Destination
@Composable
fun ExploreScreen(
    navController: NavController
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.explore),
        bottomAppBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        ExploreScreenContent(paddingValues = paddingValues)
    }
}

@Composable
fun ExploreScreenContent(
    paddingValues: PaddingValues
) {

}