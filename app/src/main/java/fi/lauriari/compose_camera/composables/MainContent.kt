package fi.lauriari.compose_camera.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.*
import fi.lauriari.compose_camera.permissions.HandleRequest
import fi.lauriari.compose_camera.permissions.PermissionDeniedContent

@ExperimentalPermissionsApi
@Composable
fun MainContent(
    permission: String,
) {
    val permissionState = rememberPermissionState(permission)

    val config = LocalConfiguration.current
    config.smallestScreenWidthDp

    var shouldShowCamera by rememberSaveable { mutableStateOf(false) }
    var shouldShowPhoto by rememberSaveable { mutableStateOf(false) }
    var photoUri by rememberSaveable { mutableStateOf("".toUri()) }

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = "To use this app's functionalities, you need to give us the permission.",
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        content = {
            if (shouldShowCamera) {
                CameraView(
                    onImageCaptured = { capturedPhotoUri ->
                        shouldShowCamera = false
                        shouldShowPhoto = true
                        photoUri = capturedPhotoUri
                    },
                    onError = {
                        shouldShowCamera = false
                    })
            } else if (shouldShowPhoto) {
                ShowPhotoAndButton(
                    onClick = {
                        shouldShowCamera = true
                        shouldShowPhoto = false
                    },
                    photoUri = photoUri
                )
            } else {
                ShowButton(
                    onClick = {
                        shouldShowCamera = true
                    }
                )
            }
        }
    )
}

@Composable
fun ShowPhotoAndButton(
    onClick: () -> Unit,
    photoUri: Uri?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(photoUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onClick
            )
            {
                Text("Toggle Camera")
            }
        }
    }
}

@Composable
fun ShowButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick
        )
        {
            Text("Toggle Camera")
        }
    }
}