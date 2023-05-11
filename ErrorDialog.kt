package com.test.sandbox

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorDialog(
    visible: Boolean,
    text: String,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(id = R.string.close)) }
        },
        title = { Text(text = stringResource(id = R.string.error)) },
        text = { Text(text = text) },
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {
    AppTheme {
        ErrorDialog(
            visible = true,
            text = "Some error",
            onDismiss = {},
        )
    }
}