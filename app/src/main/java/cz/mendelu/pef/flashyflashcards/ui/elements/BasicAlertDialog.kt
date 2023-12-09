package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.halfMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlertDialog(
    onDismissRequest: () -> Unit,
    onDeleteButtonClick: () -> Unit
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
                    .fillMaxWidth()
                    .padding(basicMargin())
            ) {
                Text(
                    text = stringResource(id = R.string.collection_dialog_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = halfMargin())
                )

                Text(
                    text = stringResource(id = R.string.collection_dialog_content),
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = onDeleteButtonClick) {
                        Text(text = stringResource(id = R.string.dialog_remove_label))
                    }

                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.dialog_dismiss_label))
                    }
                }
            }
        }
    }
}