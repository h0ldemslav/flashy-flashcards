package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold

@CollectionsNavGraph
@Destination
@Composable
fun WordsScreen(
    wordCollection: WordCollection,
    navController: NavController
) {
    BasicScaffold(
        topAppBarTitle = wordCollection.name,
        bottomAppBar = {
           BottomBar(navController = navController)
        },
        onBackClick = {
            navController.popBackStack()
        }
    ) { paddingValues ->
        WordsScreenContent(
            paddingValues = paddingValues
        )
    }
}

@Composable
fun WordsScreenContent(
    paddingValues: PaddingValues
) {

}