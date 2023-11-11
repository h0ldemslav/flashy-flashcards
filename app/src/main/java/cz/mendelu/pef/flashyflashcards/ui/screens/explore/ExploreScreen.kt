package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.UiState
import cz.mendelu.pef.flashyflashcards.extensions.getImageStarsResFromFloat
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.BusinessCategory
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.ExploreNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.DropDownElement
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@ExploreNavGraph(start = true)
@Destination
@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreScreenViewModel = hiltViewModel()
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.explore),
        bottomAppBar = {
            BottomBar(navController = navController)
        },
        showLoading = viewModel.uiState.loading
    ) { paddingValues ->
        ExploreScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            screenData = viewModel.screenData,
            actions = viewModel
        )
    }
}

@Composable
fun ExploreScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<List<Business>, ExploreErrors>,
    screenData: ExploreScreenData,
    actions: ExploreScreenActions
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val maxTextFieldCharacters = 256
    val items = BusinessCategory.values().toList().map { it._name }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(basicMargin()),
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = basicMargin())
    ) {
        Text(
            text = stringResource(id = R.string.explore_screen_info_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        BasicTextFieldElement(
            value = screenData.name,
            onValueChange = {
                if (it.length <= maxTextFieldCharacters) {
                    actions.updateScreenData(screenData.copy(name = it))
                }
            },
            label = stringResource(id = R.string.city_label),
            errorMessage = if (screenData.isValid == false)
                stringResource(id = R.string.explore_screen_city_input_error)
            else
                null
        )

        DropDownElement(
            items = items,
            selectedItem = screenData.businessCategory._name,
            label = stringResource(id = R.string.category_label),
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onDismissRequest = { isExpanded = false }
        ) {
            screenData.businessCategory = BusinessCategory.getFromString(it)
            actions.updateScreenData(screenData)
            isExpanded = false
        }

        Button(
            onClick = {
                actions.getBusinesses(screenData.name, screenData.businessCategory.alias)
            },
            modifier = Modifier.padding(top = basicMargin())
        ) {
            Text(text = stringResource(id = R.string.search_label))
        }
        
        if (uiState.data != null) {
            LazyColumn {
                uiState.data!!.forEach { business ->
                    item {
                        BusinessRow(business = business)
                    }
                }
            }
        } else if (uiState.errors != null) {
            PlaceholderElement(
                imageRes = uiState.errors!!.imageRes,
                textRes = uiState.errors!!.messageRes
            )
        }
    }
}

@Composable
fun BusinessRow(
    business: Business
) {
    Row {
        val imageRes = business.rating.getImageStarsResFromFloat()

        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Rating: ${business.rating}"
            )
        }
    }
}