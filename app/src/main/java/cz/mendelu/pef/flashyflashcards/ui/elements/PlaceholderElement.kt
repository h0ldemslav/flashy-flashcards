package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.largeMargin

const val TestTagPlaceholder = "TestTagPlaceholder"

@Composable
fun PlaceholderElement(
    @DrawableRes imageRes: Int?,
    @StringRes textRes: Int,
    fillMaxSize: Boolean = false,
    paddingValues: PaddingValues? = null
) {
    val initialPadding = paddingValues ?: PaddingValues()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(initialPadding)
            .padding(basicMargin())
            .testTag(TestTagPlaceholder)
            .then(
                if (fillMaxSize) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier.fillMaxWidth()
                }
            )
    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(220.dp)
            )
        }

        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = largeMargin())
        )
    }
}