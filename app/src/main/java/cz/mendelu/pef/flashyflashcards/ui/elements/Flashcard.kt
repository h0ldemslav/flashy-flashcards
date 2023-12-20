package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@Composable
fun Flashcard(
    text: String,
    height: Int = 156,
    onCardClick: () -> Unit
) {
    val modifiedText = if (text.length > 48) text.slice(0..47) else text

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(basicMargin()))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                onCardClick()
            }
    ) {
        Text(
            text = modifiedText,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}