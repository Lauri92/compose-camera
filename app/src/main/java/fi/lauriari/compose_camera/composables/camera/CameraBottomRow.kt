package fi.lauriari.compose_camera.composables.camera

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.FlipCameraAndroid
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material.icons.sharp.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CameraBottomRow(
    takePhoto: () -> Unit,
    switchLens: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {

            BottomRowItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Sharp.Add,
                onClick = {
                    Toast.makeText(context, "Add functionality", Toast.LENGTH_SHORT).show()
                },
                size = 50.dp
            )
            BottomRowItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Sharp.PhotoCamera,
                onClick = takePhoto,
                size = 50.dp
            )

            BottomRowItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Sharp.FlipCameraAndroid,
                onClick = switchLens,
                size = 50.dp
            )

        }
    }
}

@Composable
fun BottomRowItem(
    modifier: Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    size: Dp,
) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                modifier = Modifier.padding(bottom = 20.dp),
                onClick = onClick
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .size(size)
                )
            }
        }
    }
}