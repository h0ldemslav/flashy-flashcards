package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownElement(
    items: List<String>,
    selectedItem: String,
    label: String,
    errorMessage: String? = null,
    supportingText: String? = null,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onDropDownMenuItemClick: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = {
              Text(text = label)
            },
            supportingText = {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                } else if (supportingText != null) {
                    Text(text = supportingText)
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismissRequest
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                           Text(text = item)
                    },
                    onClick = {
                        onDropDownMenuItemClick(item)
                    }
                )
            }
        }
    }
}