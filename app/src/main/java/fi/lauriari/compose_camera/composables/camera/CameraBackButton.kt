package fi.lauriari.compose_camera.composables.camera

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CameraBackButton(
    onBackPressed: () -> Unit
) {
    BackHandler {
        onBackPressed()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onBackPressed
        ) {
            Icon(
                imageVector = Icons.Sharp.ArrowBack,
                contentDescription = "Take picture",
                tint = Color.White,
                modifier = Modifier
                    .padding(1.dp)
            )
        }
    }
}