package com.sdk.drawshape.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomDialog(setShowDialog: (Boolean) -> Unit, noBtn: () -> Unit, yesBtn: () -> Unit) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp), color = MaterialTheme.colors.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(text = "Save changes to gallery?")

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = noBtn,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(text = "No")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = yesBtn,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(text = "Yes")
                    }
                }
            }
        }
    }
}