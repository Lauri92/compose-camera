package fi.lauriari.compose_camera

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fi.lauriari.compose_camera.permissions.CameraContent
import fi.lauriari.compose_camera.ui.theme.ComposeCameraTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCameraTheme {
                CameraContent(permission = Manifest.permission.CAMERA)
            }
        }
    }
}