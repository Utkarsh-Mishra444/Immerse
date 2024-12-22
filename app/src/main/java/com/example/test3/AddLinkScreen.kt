package com.example.test3

//import androidx.compose.ui.text.input.KeyboardOptions


import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun AddLinkScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") } // URL
    var videoName by remember { mutableStateOf("") } // New field for video name
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Define the gradient background
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3A1C71), Color(0xFFD76D77), Color(0xFFFFAF7B)),
        startY = 0f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient) // Apply the gradient background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Add Link") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        OutlinedTextField(
            value = videoName,
            onValueChange = { videoName = it },
            label = { Text("Video Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        if (errorMessage != null) {
            Text(text = errorMessage ?: "", color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        }

        FloatingActionButton(
            onClick = {
                if (text.isEmpty() || !isValidYouTubeUrl(text)) {
                    errorMessage = "Please enter a valid YouTube URL"
                    return@FloatingActionButton
                }

                if (videoName.isEmpty()) {
                    errorMessage = "Please enter a video name"
                    return@FloatingActionButton
                }
                val subtitleData = SubtitleData(context)
                val existingVideoName = subtitleData.dbHelper.getVideoNameByUrl(text)
                if (existingVideoName != null) {
                    Toast.makeText(context, "URL already added under name $existingVideoName", Toast.LENGTH_LONG).show()
                    return@FloatingActionButton
                }

                coroutineScope.launch { // Launch a coroutine to handle the download process in the background when the button is clicked
                    isLoading = true
                    errorMessage = null

                    try {
                        if (!Python.isStarted()) {
                            Python.start(AndroidPlatform(context))
                        }

                        val py = Python.getInstance()
                        val pyf = py.getModule("hello") // name of your Python file

                        // Call the Python function download_auto_generated_subtitles
                        val obj = withContext(Dispatchers.IO) {
                            pyf.callAttr("download_auto_generated_subtitles", text)
                        }

                        // Convert the PyObject result to a Java String
                        val subtitles = obj.toString()

                        // Use SubtitleData to process and store subtitles in the database
                        //val subtitleData = SubtitleData(context)
                        subtitleData.processAndStoreSubtitles(text, videoName, subtitles) // Pass URL, video name, and subtitles

                        text = "" // Clear input field
                        videoName = "" // Clear video name field
                    } catch (e: Exception) {
                        errorMessage = "Failed to download subtitles: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.graphicsLayer(scaleX = animateFloatAsState(if (isLoading) 0.8f else 1f, spring(),
                label = ""
            ).value, scaleY = animateFloatAsState(if (isLoading) 0.8f else 1f, spring(), label = "").value)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Link")
        }
    }
}

fun isValidYouTubeUrl(url: String): Boolean {
    val regex = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+\$".toRegex()
    return url.matches(regex)
}

@Preview(showBackground = true)
@Composable
fun PreviewAddLinkScreen() {
    AddLinkScreen()
}
