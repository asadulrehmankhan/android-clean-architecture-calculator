package io.github.aloussase.calculator.ui

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun RectangularButton(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.height(IntrinsicSize.Max),
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = text,
            color = textColor,
        )
    }
}