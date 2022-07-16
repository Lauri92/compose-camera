package fi.lauriari.compose_camera.composables

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ScaleGestureDetector
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import fi.lauriari.compose_camera.context_extensions.getCameraProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun CameraView(
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    // 1
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraExecutor = Executors.newSingleThreadExecutor()

    // Internal app directory where to save the taken photo
    val outputDirectory = context.applicationContext.getDir("imageDir", Context.MODE_PRIVATE)

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    // 2
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }


    // 3
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        var angle by remember { mutableStateOf(0f) }
        var zoom by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        AndroidView({ previewView }, modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = zoom,
                scaleY = zoom,
                rotationZ = angle
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, gestureZoom, gestureRotate ->
                        Log.d("transformtest", "something panned")
                        angle += gestureRotate
                        zoom *= gestureZoom
                        val x = pan.x * zoom
                        val y = pan.y * zoom
                        val angleRad = angle * PI / 180.0
                        offsetX += (x * cos(angleRad) - y * sin(angleRad)).toFloat()
                        offsetY += (x * sin(angleRad) + y * cos(angleRad)).toFloat()
                    }
                )
            })

        IconButton(
            modifier = Modifier.padding(bottom = 20.dp),
            onClick = {
                Log.i("kilo", "ON CLICK")
                takePhoto(
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = onImageCaptured,
                    onError = onError
                )
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.Lens,
                    contentDescription = "Take picture",
                    tint = Color.White,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        )
    }
}

private fun takePhoto(
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
) {

    val filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS"

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions =
        ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()



    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("kilo", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val photoUri = Uri.fromFile(photoFile)
            onImageCaptured(photoUri)
        }
    })
}