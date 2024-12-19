package com.example.wordsscapegame

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.wordsscapegame.components.PermissionDialog
import com.example.wordsscapegame.components.RecordAudioPermissionTextProvider
import com.example.wordsscapegame.ui.screens.GameScreen
import com.example.wordsscapegame.ui.screens.GameViewModel
import com.example.wordsscapegame.ui.theme.WordsScapeGameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordsScapeGameTheme {
                val viewModel by viewModels<GameViewModel>()
                val permissionIsGranted by viewModel.isVoicePermissionGranted.collectAsState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { padding ->
                    GameScreen(
                        modifier = Modifier
                            .padding(
                                start = padding.calculateStartPadding(LayoutDirection.Ltr),
                                end = padding.calculateEndPadding(LayoutDirection.Rtl),
                                bottom = padding.calculateBottomPadding() - 18.dp,
                                top = padding.calculateTopPadding() - 4.dp
                            )
                    )
                }

                val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        viewModel.onPermissionGranted(isGranted)
                    }
                )
                LaunchedEffect(permissionIsGranted) {
                    requestPermission(recordAudioPermissionLauncher)
                }

                AnimatedVisibility(
                    !permissionIsGranted &&
                            !shouldShowRequestPermissionRationale(
                                Manifest.permission.RECORD_AUDIO
                            )
                ) {
                    PermissionDialog(
                        permissionProvider = RecordAudioPermissionTextProvider(context = LocalContext.current),
                        isPermanentlyDeclined = !shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO),
                        onDismiss = ::openAppSettings,
                        onConfirmation = {
                            viewModel.dismissDialog()
                            requestPermission(recordAudioPermissionLauncher)
                        },
                        onGoToAppSettings = {
                            openAppSettings()
                            viewModel.dismissDialog()
                        }
                    )
                }
            }
        }
    }

    private fun requestPermission(recordAudioPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>) =
        run {
            recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordsScapeGameTheme {
        GameScreen()
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}