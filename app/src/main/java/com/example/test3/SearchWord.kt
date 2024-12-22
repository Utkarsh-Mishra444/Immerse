package com.example.test3


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test3.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchWord : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchWordScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWordScreen(viewModel: SearchWordViewModel = viewModel(factory = SearchWordViewModelFactory(LocalContext.current))) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val context = LocalContext.current
    AppTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.performSearch(query)
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                //OutlinedTextFieldDefaults.colors()



//                outlinedTextFieldColors(
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.White,
//                    textColor = Color.White,
//                    cursorColor = Color.White,
//                    focusedLabelColor = Color.White,
//                    unfocusedLabelColor = Color.White
//                ),

            )

            IconButton(
                onClick = { viewModel.performSearch(query) }
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
        }

        LazyColumn {
            items(searchResults) { url ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.setPackage("com.google.android.youtube")
                            context.startActivity(intent)
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                ) {
                    Row(
                        modifier = Modifier
                            //.background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .padding(8.dp),
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = url,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
}
@Composable
@Preview(showBackground = true)
fun PreviewSearchWordScreen() {
    SearchWordScreen()
}

// ViewModelFactory to pass context to ViewModel
class SearchWordViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchWordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchWordViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// ViewModel to handle search logic
class SearchWordViewModel(private val context: Context) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val dbHelper = SubtitleDatabaseHelper(context)

    fun performSearch(query: String) {
        viewModelScope.launch {
            _searchResults.value = emptyList()
            val results = withContext(Dispatchers.IO) {
                //val cursor = dbHelper.getWordTimestamps(query)
                //val cursor = dbHelper.getWordTimestampsForAllFiles(query) // Use the new helper method


                val cursor = dbHelper.getWordTimestampsForAllFilesMulti(query) // Use the new helper method


                val urls = mutableListOf<String>()

                cursor.use {
                    if (cursor.moveToFirst()) {
                        val urlIndex = cursor.getColumnIndexOrThrow(SubtitleDatabaseHelper.COLUMN_URL)
                        val timestampIndex = cursor.getColumnIndexOrThrow(SubtitleDatabaseHelper.COLUMN_TIMESTAMP)

                        while (!cursor.isAfterLast) {
                            val url = cursor.getString(urlIndex)
                            val timestamp = cursor.getInt(timestampIndex)
                            urls.add("$url&t=${timestamp}s")
                            cursor.moveToNext()
                        }
                    }
                }
                // Log fetched results
                Log.d("SearchDebug", "Fetched results: $urls")
                urls
            }
            // Log before updating search results
            Log.d("SearchDebug", "Updating search results: $results")
            _searchResults.value = results
        }
    }
    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
}
