package com.example.wordsscapegame.presentation.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wordsscapegame.R
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * A dialog that displays information about a required permission and provides options
 * for the user to grant the permission or navigate to app settings.
 *
 * This dialog is typically used when an app needs to request a permission that has been
 * denied or permanently declined by the user. It explains why the permission is needed
 * and provides clear actions for the user to take.
 *
 * @param modifier Modifier to be applied to the dialog.
 * @param permissionProvider An instance of PermissionTextProvider that provides
 * descriptive text about the required permission.
 * @param isPermanentlyDeclined Indicates whether the permission has been permanently
 * declined by the user.
 * @param onDismiss The action to perform when the dialog is dismissed.
 * @param onConfirmation The action to perform when the user confirms the permission
 * request (e.g., by clicking "OK").
 * @param onGoToAppSettings The action to perform when the user chooses to navigate
 * to the app's settings to grant the permission manually.
 */
@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    permissionProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    onGoToAppSettings: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.permission_required),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = permissionProvider.getDescription(
                        isPermanentlyDeclined = isPermanentlyDeclined
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(20.dp)

                )
                Spacer(modifier = Modifier.height(16.dp))
                ActionButton(
                    modifier = Modifier
                        .size(width = 132.dp, height = 68.dp),
                    text = if (isPermanentlyDeclined) {
                        stringResource(R.string.grant_permission)
                    } else {
                        stringResource(R.string.ok)
                    },
                    textStyle = MaterialTheme.typography.labelSmall,
                    onClicked = {
                        if (isPermanentlyDeclined) {
                            onGoToAppSettings()
                        } else {
                            onConfirmation()
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    PermissionDialog(
        permissionProvider = RecordAudioPermissionTextProvider(context = LocalContext.current),
        isPermanentlyDeclined = false,
        onDismiss = {},
        onConfirmation = {},
        onGoToAppSettings = {}
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class RecordAudioPermissionTextProvider(@ApplicationContext private val context: Context) :
    PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.permanently_declined_record_audio_permission_message)
        } else {
            context.getString(R.string.declined_record_audio_permission_message)
        }
    }
}