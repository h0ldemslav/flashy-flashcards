package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.flashyflashcards.ui.theme.PinkPrimaryLight
import cz.mendelu.pef.flashyflashcards.ui.theme.largeMargin

@Composable
fun LoadingScreen(
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        LoadingScreenCircleIndicator(
            modifier = Modifier.padding(largeMargin())
        )
    }
}

@Composable
fun LoadingScreenCircleIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        color = PinkPrimaryLight,
        strokeWidth = 5.dp,
        modifier = modifier
    )
}
