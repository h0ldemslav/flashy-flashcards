package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BasicTextFieldElement(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    supportingText: String? = null,
    errorMessage: String? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
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
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}