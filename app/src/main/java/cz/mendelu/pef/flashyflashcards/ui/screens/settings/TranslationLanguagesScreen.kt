package cz.mendelu.pef.flashyflashcards.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.navigation.graphs.SettingsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.ListRow
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@SettingsNavGraph
@Destination
@Composable
fun TranslationLanguagesScreen(
    navController: NavController,
    viewModel: TranslationLanguagesScreenViewModel = hiltViewModel()
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.translation_languages),
        showLoading = viewModel.uiState.loading,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        TranslationLanguagesScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            onListRowIconClick = { code ->
                viewModel.deleteTranslationModel(code)
            }
        )
    }
}

@Composable
fun TranslationLanguagesScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<Map<String, String>, ScreenErrors>,
    onListRowIconClick: (String) -> Unit
) {
    val context = LocalContext.current

    if (uiState.data?.isNotEmpty() == true) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
        ) {
            uiState.data!!.forEach { (code, name) ->
                item {
                    ListRow(
                        showAvatar = false,
                        headline = name,
                        actionIcon = Icons.Default.Delete,
                        actionIconDescription = stringResource(id = R.string.delete_label),
                        onActionIconClick = {
                            onListRowIconClick(code)
                        }
                    )
                }
            }
        }

        if (uiState.errors?.messageRes == R.string.failed_to_delete_downloaded_language) {
            Toast.makeText(
                context,
                context.getString(uiState.errors!!.messageRes),
                Toast.LENGTH_SHORT
            ).show()
        }
    } else {
        PlaceholderElement(
            imageRes = uiState.errors?.imageRes,
            textRes = uiState.errors?.messageRes ?: R.string.empty_placeholder,
            paddingValues = paddingValues,
            fillMaxSize = true
        )
    }
}