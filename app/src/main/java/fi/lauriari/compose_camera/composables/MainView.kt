package fi.lauriari.compose_camera.composables

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
    imageListState: LazyListState
) {

    val context = LocalContext.current

    val imageDir: File = context.getDir("imageDir", Context.MODE_PRIVATE).absoluteFile
    val imageList = imageDir.list()

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
            LazyRow(
                modifier = Modifier.padding(20.dp),
                state = imageListState
            ) {
                items(imageList) { uri ->
                    Image(
                        modifier = Modifier
                            .size(150.dp)
                            .clickable {
                                val file = File(imageDir, uri)
                                file.delete()
                            },
                        painter = rememberAsyncImagePainter("${Constants.saveDirectoryUri}$uri".toUri()),
                        contentDescription = null
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
