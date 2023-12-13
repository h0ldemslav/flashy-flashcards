package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@CollectionsNavGraph
@Destination
@Composable
fun AddEditWordScreen(
    navController: NavController
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.new_word),
        onBackClick = { navController.popBackStack() },
        bottomAppBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        AddEditWordScreenContent(paddingValues = paddingValues)
    }
}

@Composable
fun AddEditWordScreenContent(
    paddingValues: PaddingValues
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(basicMargin()),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = basicMargin())
    ) {
        BasicTextFieldElement(
            value = "Word",
            onValueChange = {

            },
            label = stringResource(id = R.string.name_label)
        )

        BasicTextFieldElement(
            value = "Slovo",
            onValueChange = {

            },
            label = stringResource(id = R.string.translation_label)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(basicMargin()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = basicMargin())
                .padding(horizontal = basicMargin())
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(0.4f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = stringResource(id = R.string.translation_label))
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(0.4f)
            ) {
                Text(text = stringResource(id = R.string.save_label))
            }
        }
    }
}