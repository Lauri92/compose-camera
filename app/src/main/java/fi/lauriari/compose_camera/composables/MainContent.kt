package fi.lauriari.compose_camera.composables

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import fi.lauriari.compose_camera.permissions.HandleRequest
import fi.lauriari.compose_camera.permissions.PermissionDeniedContent
import java.io.File

@ExperimentalPermissionsApi
@Composable
fun MainContent(
    permission: String,
) {
    val permissionState = rememberPermissionState(permission)

    var shouldShowCamera by rememberSaveable { mutableStateOf(false) }
    var shouldShowPhoto by rememberSaveable { mutableStateOf(false) }
    var shouldShowImageList by rememberSaveable { mutableStateOf(false) }
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
                    },
                    onClick2 = {
                        shouldShowImageList = true
                    },
                    shouldShowImageList = shouldShowImageList
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
            ) {
                Text("Toggle Camera")
            }
        }
    }
}

@Composable
fun ShowButton(
    onClick: () -> Unit,
    onClick2: () -> Unit,
    shouldShowImageList: Boolean
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onClick
            ) {
                Text("Toggle Camera")
            }
            Button(
                onClick = onClick2
            ) {
                Text("Images")
            }
        }

        AnimatedVisibility(visible = shouldShowImageList) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Log.d("filestest", "Got here")
                val imageDir: File = context.getDir("imageDir", Context.MODE_PRIVATE)
                val directory: File = imageDir.absoluteFile

                for (file in directory.list()!!) {
                    Log.d("filestest", file)
                    val fileUri: Uri =
                        "file:///data/user/0/fi.lauriari.compose_camera/app_imageDir/$file".toUri()
                    Image(
                        painter = rememberAsyncImagePainter(fileUri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(20.dp)
                    )
                }
            }
        }
    }
}