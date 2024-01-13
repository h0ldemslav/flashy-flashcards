package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.extensions.getImageStarsResFromFloat
import cz.mendelu.pef.flashyflashcards.extensions.isAtBottom
import cz.mendelu.pef.flashyflashcards.extensions.isDark
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.BusinessCategory
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.ExploreNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.DropDownElement
import cz.mendelu.pef.flashyflashcards.ui.elements.LoadingScreenCircleIndicator
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.BookmarksScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.DetailScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.halfMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.smallMargin

const val TestTagNameTextField = "TestTagNameTextField"
const val TestTagSearchButton = "TestTagSearchButton"
const val TestTagSearchResultsList = "TestTagSearchResultsList"

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
        actions = {
            IconButton(onClick = {
                navController.navigate(BookmarksScreenDestination)
            }) {
                Icon(
                    imageVector = Icons.Default.Bookmarks,
                    contentDescription = stringResource(id = R.string.img_alt_bookmarks)
                )
            }
        }
    ) { paddingValues ->
        ExploreScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            uiState = viewModel.uiState,
            screenData = viewModel.screenData,
            actions = viewModel
        )
    }
}

@Composable
fun ExploreScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    uiState: UiState<MutableList<Business>, ScreenErrors>,
    screenData: ExploreScreenData,
    actions: ExploreScreenActions
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxTextFieldCharacters = 256
    // Don't change the order of `dropDownItems`, because in `DropDownElement` indexes are used
    // for updating category in screen data, see below
    val dropDownItems = actions.getBusinessCategoryDisplayNames().map { stringResource(id = it) }

    val lazyListState: LazyListState = rememberLazyListState()
    val isAtBottom = lazyListState.isAtBottom()
    
    LaunchedEffect(isAtBottom) {
        if (isAtBottom && uiState.data?.isNotEmpty() == true) {
            actions.getAnotherPlaces()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(basicMargin()),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = basicMargin())
    ) {
        Text(
            text = stringResource(id = R.string.explore_screen_info_text),
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
            errorMessage = if (screenData.errorMessage == R.string.explore_screen_city_input_error)
                stringResource(id = screenData.errorMessage!!)
            else
                null,
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            modifier = Modifier.testTag(TestTagNameTextField)
        )

        DropDownElement(
            items = dropDownItems,
            selectedItem = stringResource(id = screenData.businessCategory.displayName),
            label = stringResource(id = R.string.category_label),
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onDismissRequest = { isExpanded = false }
        ) { index, _ ->
            actions.updateScreenData(
                screenData.copy(businessCategory = BusinessCategory.values()[index])
            )
            isExpanded = false
        }

        Button(
            enabled = !uiState.loading,
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                actions.searchPlaces()
            },
            modifier = Modifier
                .padding(top = basicMargin())
                .padding(bottom = halfMargin())
                .testTag(TestTagSearchButton)
        ) {
            Text(text = stringResource(id = R.string.search_label))
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.testTag(TestTagSearchResultsList)
        ) {
            uiState.data?.forEach { business ->
                item {
                    BusinessRow(business = business) {
                        actions.cacheBusiness(business)
                        navController.navigate(
                            DetailScreenDestination(
                                dataSourceType = DataSourceType.Remote(business.remoteId)
                            )
                        )
                    }
                }
            }

            if (uiState.loading) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingScreenCircleIndicator(
                            modifier = Modifier.padding(vertical = basicMargin())
                        )
                    }
                }
            }
        }

        if (uiState.errors != null) {
            PlaceholderElement(
                imageRes = uiState.errors!!.imageRes,
                textRes = uiState.errors!!.messageRes
            )
        }
    }
}

@Composable
fun BusinessRow(
    business: Business,
    onRowClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onRowClick() }
        .padding(vertical = 12.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(business.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.undraw_page_not_found),
            placeholder = painterResource(id = R.drawable.undraw_photos),
            modifier = Modifier
                .height(64.dp)
                .width(114.dp)
                .padding(top = basicMargin())
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = basicMargin())
        ) {
            Text(
                text = if (business.name.length > 24)
                    business.name.slice(0..23) + "..."
                else
                    business.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = if (business.category.length > 16)
                    business.category.slice(0..15) + "..."
                else
                    business.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(basicMargin()),
                modifier = Modifier
                    .height(28.dp)
                    .fillMaxWidth()
                    .padding(top = smallMargin())
            ) {
                val imageRes = business.rating.getImageStarsResFromFloat()
                val yelpLogo = if (MaterialTheme.colorScheme.isDark())
                    R.drawable.yelp_logo_dark
                else
                    R.drawable.yelp_logo_light

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = business.rating,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Image(
                    painter = painterResource(id = yelpLogo),
                    contentDescription = stringResource(id = R.string.yelp_logo),
                    alignment = Alignment.TopEnd,
                    modifier = Modifier.size(52.dp)
                )
            }

            Text(
                text = "${business.reviewCount} ${stringResource(id = R.string.reviews).lowercase()}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}