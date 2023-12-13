package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.AddEditWordScreenDestination

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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(AddEditWordScreenDestination)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
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