package fi.lauriari.compose_camera.composables.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import fi.lauriari.compose_camera.context_extensions.getCameraProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@Composable
fun CameraView(
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    onBackPressed: () -> Unit
) {
    Log.d("cameratrace", "Before value change")
    // Which lens should be used
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }


    // Image quality, range [1..100]
    val imageQuality = 50

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraExecutor = Executors.newSingleThreadExecutor()

    // Internal app directory where to save the taken photo
    val outputDirectory = context.applicationContext.getDir("imageDir", Context.MODE_PRIVATE)

    val cameraController = LifecycleCameraController(context)
    cameraController.bindToLifecycle(lifecycleOwner)

    // Camera preview displayed on screen
    // TODO: Set aspect ratio to 16:9?
    val preview = Preview.Builder()
        .build()
    val previewView = remember {
        PreviewView(context).also {
            it.scaleType = PreviewView.ScaleType.FIT_CENTER
            it.controller = cameraController
        }
    }

    // Builder for captured image
    // TODO: Set aspect ratio to 16:9?
    val imageCapture: ImageCapture =
        remember {
            ImageCapture.Builder()
                .setJpegQuality(imageQuality)
                .build()
        }

    // Which lens should be used
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()


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

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        AndroidView(
            { previewView }, modifier = Modifier
                .fillMaxSize()
        )

        CameraBackButton(onBackPressed = onBackPressed)

        CameraBottomRow(
            takePhoto = {
                takePhoto(
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = onImageCaptured,
                    onError = onError
                )
            },
            switchLens = {
                lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                    CameraSelector.LENS_FACING_BACK
                } else {
                    CameraSelector.LENS_FACING_FRONT
                }
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
        SimpleDateFormat(
            filenameFormat,
            Locale.getDefault()
        ).format(System.currentTimeMillis()) + ".jpg"
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