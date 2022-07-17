package fi.lauriari.compose_camera.composables

import android.content.Context
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import fi.lauriari.compose_camera.util.Constants
import java.io.File

@Composable
fun MainView(
    onToggleCameraClick: () -> Unit,
    onToggleImagesClick: () -> Unit,
    shouldShowImageList: Boolean,
    photoUri: Uri,
    imagesScrollState: ScrollState
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = rememberAsyncImagePainter(photoUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
        AnimatedVisibility(visible = shouldShowImageList) {
            Row(
                modifier = Modifier
                    .horizontalScroll(imagesScrollState)
            ) {
                val imageDir: File = context.getDir("imageDir", Context.MODE_PRIVATE).absoluteFile

                for (file in imageDir.list()!!) {
                    Image(
                        painter = rememberAsyncImagePainter("${Constants.saveDirectoryUri}$file".toUri()),
                        contentDescription = null,
                        modifier = Modifier
                            .size(250.dp)
                            .padding(20.dp)
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = onToggleCameraClick
            ) {
                Text("Toggle Camera")
            }
            Button(
                onClick = onToggleImagesClick
            ) {
                Text("Images")
            }
        }
    }
}
