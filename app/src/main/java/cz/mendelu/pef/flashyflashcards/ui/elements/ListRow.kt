package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.halfMargin

@Composable
fun ListRow(
    showAvatar: Boolean = true,
    headline: String,
    supportingText: String? = null,
    actionIcon: ImageVector? = null,
    actionIconDescription: String? = null,
    onActionIconClick: () -> Unit = {},
    onRowClick: () -> Unit = {}
) {
    val _headline = if (headline.length > 24) {
        headline.slice(0..23) + "..."
    } else {
        headline
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = halfMargin())
        .clickable { onRowClick() }
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showAvatar) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = headline.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(start = basicMargin())
        ) {
            Text(
                text = _headline,
                style = MaterialTheme.typography.bodyLarge
            )

            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        if (actionIcon != null) {
            IconButton(onClick = { onActionIconClick() }) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconDescription
                )
            }
        }
    }
}