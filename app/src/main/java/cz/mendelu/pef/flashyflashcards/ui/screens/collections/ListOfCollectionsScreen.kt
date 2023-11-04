package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold

@CollectionsNavGraph(start = true)
@Destination
@Composable
fun ListOfCollectionsScreen(
    navController: NavController
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.collections),
        bottomAppBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
       ListOfCollectionsScreenContent(paddingValues = paddingValues)
    }
}

@Composable
fun ListOfCollectionsScreenContent(
    paddingValues: PaddingValues
) {

}