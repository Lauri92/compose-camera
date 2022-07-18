package fi.lauriari.compose_camera.composables

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import fi.lauriari.compose_camera.composables.camera.CameraView
import fi.lauriari.compose_camera.permissions.HandleRequest
import fi.lauriari.compose_camera.permissions.PermissionDeniedContent

@ExperimentalPermissionsApi
@Composable
fun MainContent(
    permission: String,
) {
    val permissionState = rememberPermissionState(permission)

    var shouldShowCamera by rememberSaveable { mutableStateOf(false) }
    var shouldShowImageList by rememberSaveable { mutableStateOf(false) }
    var photoUri by rememberSaveable { mutableStateOf("".toUri()) }
    val imageListState = rememberLazyListState()

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
                        photoUri = capturedPhotoUri
                    },
                    onError = {
                        shouldShowCamera = false
                    },
                    onBackPressed = {
                        shouldShowCamera = false
                    })
            } else {
                MainView(
                    onToggleCameraClick = { shouldShowCamera = !shouldShowCamera },
                    onToggleImagesClick = { shouldShowImageList = !shouldShowImageList },
                    shouldShowImageList = shouldShowImageList,
                    photoUri = photoUri,
                    imageListState = imageListState
                )
            }
        }
    )
}