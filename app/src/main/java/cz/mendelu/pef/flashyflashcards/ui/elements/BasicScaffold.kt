package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.flashyflashcards.R

const val TestTagBackButton = "TestTagBackButton"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScaffold(
    topAppBarTitle: String,
    bottomAppBar: @Composable () -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    showLoading: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = topAppBarTitle) },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.testTag(TestTagBackButton)
                        ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.navigation_icon_back)
                                )
                        }
                    }
                },
                actions = actions,
            )
        },
        bottomBar = bottomAppBar,
        floatingActionButton = floatingActionButton
    ) { paddingValues ->
        if (showLoading) {
            LoadingScreen(modifier = Modifier.padding(paddingValues))
        } else {
            content(paddingValues)
        }
    }
}