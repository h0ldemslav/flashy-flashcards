package cz.mendelu.pef.flashyflashcards.ui.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.SettingsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold

@SettingsNavGraph(start = true)
@Destination
@Composable
fun SettingsScreen(
    navController: NavController
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.settings),
        bottomAppBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        SettingsScreenContent(paddingValues = paddingValues)
    }
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues
) {

}