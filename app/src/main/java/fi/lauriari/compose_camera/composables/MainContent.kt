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
import androidx.compose.ui.unit.dp
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

    val shouldShowCamera = rememberSaveable { mutableStateOf(false) }
    val shouldShowPhoto = rememberSaveable { mutableStateOf(false) }
    var photoUri: Uri? = null

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
            if (shouldShowCamera.value) {
                CameraView(
                    onImageCaptured = { capturedPhotoUri ->
                        shouldShowCamera.value = false
                        shouldShowPhoto.value = true
                        photoUri = capturedPhotoUri
                    },
                    onError = {
                        shouldShowCamera.value = false
                    })
            } else if (shouldShowPhoto.value) {
                ShowPhotoAndButton(
                    shouldShowCamera = shouldShowCamera,
                    shouldShowPhoto = shouldShowPhoto,
                    photoUri = photoUri
                )
            } else {
                ShowButton(shouldShowCamera = shouldShowCamera)
            }
        }
    )
}

@Composable
fun ShowPhotoAndButton(
    shouldShowCamera: MutableState<Boolean>,
    shouldShowPhoto: MutableState<Boolean>,
    photoUri: Uri?
) {
    Column(
        modifier = Modifier.fillMaxSize()
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
                onClick = {
                    shouldShowCamera.value = true
                    shouldShowPhoto.value = false
                })
            {
                Text("Toggle Camera")
            }
        }
    }
}

@Composable
fun ShowButton(shouldShowCamera: MutableState<Boolean>) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                shouldShowCamera.value = true
            })
        {
            Text("Toggle Camera")
        }
    }
}