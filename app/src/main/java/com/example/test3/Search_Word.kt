package com.example.test3
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.compose.foundation.lazy.LazyColumn
//
//class Search_Word : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SearchWordScreen()
//        }
//    }
//}
//
//@Composable
//fun SearchWordScreen(viewModel: SearchWordViewModel = viewModel()) {
//    var query by remember { mutableStateOf("") }
//    val searchResults by viewModel.searchResults.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = query,
//                onValueChange = { query = it },
//                label = { Text("Search") },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(end = 8.dp),
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    imeAction = ImeAction.Search
//                ),
//                keyboardActions = KeyboardActions(
//                    onSearch = {
//                        viewModel.performSearch(query)
//                    }
//                )
//            )
//
//            IconButton(
//                onClick = { viewModel.performSearch(query) }
//            ) {
//                Icon(Icons.Default.Search, contentDescription = "Search")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LazyColumn {
//            items(searchResults) { url ->
//                Text(url, modifier = Modifier.padding(8.dp))
//            }
//        }
//    }
//}
//
//@Composable
//@Preview(showBackground = true)
//fun PreviewSearchWordScreen() {
//    SearchWordScreen()
//}
//
//// ViewModel to handle search logic
//class SearchWordViewModel : ViewModel() {
//    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
//    val searchResults: StateFlow<List<String>> = _searchResults
//
//    fun performSearch(query: String) {
//        val timestamps = SubtitleData.getInstance().getTimestamps(query)
//        val originalUrl = SubtitleData.getInstance().originalUrl
//
//        val urls = timestamps.map { timestamp ->
//            "$originalUrl&t=${timestamp}s"
//        }
//
//        _searchResults.value = urls
//    }
//}
//
//// Stub of the SubtitleData class for demonstration purposes
//object SubtitleData {
//    fun getInstance(): SubtitleData = this
//    val originalUrl: String = "https://example.com/video"
//    fun getTimestamps(query: String): List<Int> {
//        // Mock implementation
//        return listOf(10, 20, 30)
//    }
//}
//
//// UrlAdapter class is not needed in Jetpack Compose since we directly use LazyColumn
