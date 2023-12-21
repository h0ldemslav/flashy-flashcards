package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

@Composable
fun BasicTimer(
    modifier: Modifier = Modifier,
    totalTimeInMillis: Long,
    textAlign: TextAlign = TextAlign.End,
    onTimeChange: (Long) -> Unit
) {
    LaunchedEffect(totalTimeInMillis) {
        if (totalTimeInMillis > 0) {
            delay(100L)
            onTimeChange(totalTimeInMillis)
        }
    }

    Text(
        text = (totalTimeInMillis / 1000L).toString(),
        textAlign = textAlign,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}