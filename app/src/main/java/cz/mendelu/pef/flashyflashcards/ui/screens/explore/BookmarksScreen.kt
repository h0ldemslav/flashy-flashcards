package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.ExploreNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.ListRow
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.DetailScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.mediumMargin

@ExploreNavGraph
@Destination
@Composable
fun BookmarksScreen(
    navController: NavController,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.bookmarks),
        onBackClick = { navController.popBackStack() },
        bottomAppBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        BookmarksScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            uiState = viewModel.uiState,
            onListRowAppear = { word, business ->
                viewModel.filterByWord(word, business)
            },
            searchInput = viewModel.searchInput,
            onSearchInputChange = {
                viewModel.searchInput = it
            }
        )
    }
}

@Composable
fun BookmarksScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    uiState: UiState<List<Business>, ScreenErrors>,
    onListRowAppear: (String, Business) -> Boolean,
    searchInput: String,
    onSearchInputChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = basicMargin())
    ) {
        if (uiState.data?.isNotEmpty() == true) {
            BasicTextFieldElement(
                value = searchInput,
                onValueChange = {
                    onSearchInputChange(it)
                },
                label = stringResource(id = R.string.search_label),
                supportingText = stringResource(id = R.string.bookmarks_search_field_hint),
                onDone = {
                    focusManager.clearFocus()
                }
            )

            Spacer(modifier = Modifier.height(mediumMargin()))

            LazyColumn {
                uiState.data!!.forEach { business ->
                    if (onListRowAppear(searchInput, business)) {
                        item {
                            ListRow(headline = business.name) {
                                navController.navigate(
                                    DetailScreenDestination(
                                        dataSourceType = DataSourceType.Local(business.remoteId)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            PlaceholderElement(
                imageRes = uiState.errors?.imageRes,
                textRes = uiState.errors?.messageRes ?: R.string.empty_placeholder,
                fillMaxSize = true
            )
        }
    }
}