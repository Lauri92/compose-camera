package fi.lauriari.compose_camera

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fi.lauriari.compose_camera.composables.MainContent
import fi.lauriari.compose_camera.ui.theme.ComposeCameraTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCameraTheme {
                MainContent(permission = Manifest.permission.CAMERA)
            }
        }
    }
}