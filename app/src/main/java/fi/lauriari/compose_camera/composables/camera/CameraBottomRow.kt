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
import androidx.compose.material.icons.sharp.SwitchCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun CameraBottomRow(
    takePhoto: () -> Unit
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

            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        modifier = Modifier.padding(bottom = 20.dp),
                        onClick = {
                            Toast.makeText(context, "Add some functionality", Toast.LENGTH_SHORT)
                                .show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Add,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    modifier = Modifier.padding(bottom = 20.dp),
                    onClick = takePhoto
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Lens,
                        contentDescription = "Take picture",
                        tint = Color.White,
                        modifier = Modifier
                            .size(75.dp)
                            .padding(1.dp)
                            .border(1.dp, Color.White, CircleShape)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        modifier = Modifier.padding(bottom = 20.dp),
                        onClick = {
                            Toast.makeText(context, "Add some functionality", Toast.LENGTH_SHORT)
                                .show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.FlipCameraAndroid,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomRowItem() {

}