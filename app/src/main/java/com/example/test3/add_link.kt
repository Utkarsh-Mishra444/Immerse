package com.example.test3
//
//import android.os.Bundle
//import android.view.View
//import android.widget.EditText
//import android.widget.ImageButton
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.chaquo.python.Python
//import com.chaquo.python.android.AndroidPlatform
//import kotlin.math.min
//
//class add_link : AppCompatActivity() {
//    /**
//     * This method is called when the activity is starting.
//     * It is where most initialization should go.
//     *
//     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
//     * then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
//     * Note: Otherwise it is null.
//     */
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState) // Call the superclass method
//
//        // Enable edge-to-edge screen support. This makes the layout extend into the window insets.
//        this.enableEdgeToEdge()
//
//        // Set the activity content from a layout resource. The resource will be inflated, adding all top-level views to the activity.
//        setContentView(R.layout.add_link)
//
//        if (!Python.isStarted()) {
//            Python.start(AndroidPlatform(this))
//        }
//
//        val py = Python.getInstance()
//        val pyf = py.getModule("hello") // name of your Python file
//
//        // Get the YouTube URL from the TextInputEditText
//        val ytlink = findViewById<EditText>(R.id.ytlink)
//
//        // Get the button from the layout
//        val button = findViewById<ImageButton>(R.id.plus_button) // Replace with your button's ID
//        val youtube_url = ytlink.text.toString()
//
//        button.setOnClickListener {
//            val youtube_url = ytlink.text.toString()
//            // Call the Python function download_auto_generated_subtitles
//            val obj = pyf.callAttr("download_auto_generated_subtitles", youtube_url)
//
//            // Convert the PyObject result to a Java String
//            val subtitles = obj.toString()
//
//            // Store the subtitles in the Singleton class
//            SubtitleData.getInstance().setSubtitles(subtitles)
//            SubtitleData.getInstance().originalUrl = youtube_url // Set the original URL
//
//            val message = "Video added successfully: " + subtitles.substring(
//                0, min(youtube_url.length.toDouble(), 10.0)
//                    .toInt()
//            )
//            Toast.makeText(this@add_link, message, Toast.LENGTH_LONG).show()
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}