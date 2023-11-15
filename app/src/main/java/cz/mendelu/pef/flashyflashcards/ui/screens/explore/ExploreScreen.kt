package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import cz.mendelu.pef.flashyflashcards.ui.theme.halfMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.smallMargin

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

    val controller = LocalSoftwareKeyboardController.current

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
                    actions.updateScreenData(screenData.copy(name = it, isValid = null))
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
            actions.updateScreenData(screenData.copy())
            isExpanded = false
        }

        Button(
            onClick = {
                controller?.hide()
                actions.searchPlaces()
            },
            modifier = Modifier
                .padding(top = basicMargin())
                .padding(bottom = halfMargin())
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
    Row(modifier = Modifier
        .fillMaxWidth()
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
            if (business.name.length > 24) {
                business.name = business.name.slice(0..23) + "..."
            }

            Text(
                text = business.name,
                style = MaterialTheme.typography.bodyLarge
            )

            if (business.category.length > 16) {
                business.category = business.category.slice(0..15) + "..."
            }

            Text(
                text = business.category,
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
                val yelpLogo = if (isSystemInDarkTheme())
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