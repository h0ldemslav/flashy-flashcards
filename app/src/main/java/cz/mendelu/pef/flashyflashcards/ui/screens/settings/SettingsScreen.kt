package cz.mendelu.pef.flashyflashcards.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.flashyflashcards.BuildConfig
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.AppPreference
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceValue
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.SettingsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.ListRow
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.screens.destinations.TranslationLanguagesScreenDestination
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.halfMargin

@SettingsNavGraph(start = true)
@Destination
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.settings),
        showLoading = viewModel.uiState.loading,
        bottomAppBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        SettingsScreenContent(
            paddingValues = paddingValues,
            navController = navController,
            uiState = viewModel.uiState,
            actions = viewModel
        )
    }
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    uiState: UiState<List<AppPreference>, ScreenErrors>,
    actions: SettingsScreenActions
) {
    var currentPreference by remember {
        mutableStateOf<AppPreference?>(null)
    }
    val appVersionName = BuildConfig.VERSION_NAME

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = basicMargin()),
        verticalArrangement = Arrangement.spacedBy(basicMargin())
    ) {
        if (uiState.data != null) {
            uiState.data!!.forEach { appPreference ->
                ListRow(
                    showAvatar = false,
                    headline = stringResource(id = appPreference.displayName),
                    supportingText = stringResource(id = appPreference.displayValue)
                ) {
                    currentPreference = appPreference
                }
            }

            Text(
                text = stringResource(id = R.string.downloaded_languages),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = basicMargin())
                    .clickable {
                        navController.navigate(TranslationLanguagesScreenDestination)
                    }
            )

            Text(
                text = stringResource(id = R.string.application_version) + ": $appVersionName",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = halfMargin())
            )

            if (currentPreference != null) {
                PreferenceDialog(
                    appPreference = currentPreference!!,
                    appPreferenceValues = actions.getAllAppPreferenceValues(currentPreference!!),
                    onOptionClick = { displayName, displayValue ->
                        actions.updateAppPreference(displayName, displayValue)
                        currentPreference = null
                    }
                ) {
                    currentPreference = null
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceDialog(
    appPreference: AppPreference,
    appPreferenceValues: List<AppPreferenceValue>,
    onOptionClick: (Int, Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .padding(basicMargin())
            ) {
                Text(
                    text = stringResource(id = appPreference.displayName),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = halfMargin())
                )

                appPreferenceValues.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionClick(appPreference.displayName, option.displayName)
                            }
                            .padding(bottom = halfMargin()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option.displayName == appPreference.displayValue,
                            onClick = null,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = stringResource(id = option.displayName),
                            modifier = Modifier.padding(start = halfMargin()),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = stringResource(id = R.string.cancel_label),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }
        }
    }
}
