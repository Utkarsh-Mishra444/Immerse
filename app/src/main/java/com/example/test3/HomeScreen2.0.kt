    package com.example.test3

    import androidx.compose.animation.ExperimentalAnimationApi
    import androidx.compose.animation.core.animateDpAsState
    import androidx.compose.animation.core.animateFloatAsState
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ArrowForward
    import androidx.compose.material.icons.filled.Add
    import androidx.compose.material.icons.filled.Close
    import androidx.compose.material.icons.filled.Edit
    import androidx.compose.material.icons.filled.MoreVert
    import androidx.compose.material.icons.filled.Refresh
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material3.BottomAppBar
    import androidx.compose.material3.BottomAppBarDefaults
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.ListItem
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.SearchBar
    import androidx.compose.material3.SearchBarDefaults
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.graphicsLayer
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import androidx.navigation.compose.rememberNavController
    import com.example.test3.theme.AppTheme

    enum class Screens {
        HomeScreen,
        SearchWord,
        Learn,
        AddLink
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen2(navController: NavController) {
        var searchText by remember { mutableStateOf("") }
        var expanded by rememberSaveable { mutableStateOf(false) }
        val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
        val dictionarySearchViewModel: DictionarySearchViewModel = viewModel(
            factory = DictionarySearchViewModelFactory(LocalContext.current)
        )
        val searchResults by dictionarySearchViewModel.searchResults.collectAsState()
        val padding by animateDpAsState(targetValue = if (expanded) 0.dp else 16.dp)

        AppTheme {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        actions = {
                            IconButton(onClick = {}) {
                                Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate("addLinkScreen") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    SearchBar(
                        modifier = Modifier
                            .padding(horizontal = padding)
                            .fillMaxWidth(),
                        inputField = {
                            SearchBarDefaults.InputField(
                                modifier = Modifier.size(300.dp, 56.dp).align(Alignment.CenterHorizontally),
                                query = searchText,
                                onQueryChange = {
                                    dictionarySearchViewModel.clearSearchResults()
                                    searchText = it
                                    dictionarySearchViewModel.performSearch(searchText)
                                },
                                onSearch = {
                                    expanded = true
                                    dictionarySearchViewModel.clearSearchResults()
                                    dictionarySearchViewModel.performSearch(searchText)
                                },
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                placeholder = { Text("Enter Word") },
                                leadingIcon = {
                                    AnimatedSearchToBackRotated(
                                        expanded = expanded,
                                        onClick = {
                                            if (expanded) {
                                                expanded = false
                                                dictionarySearchViewModel.clearSearchResults()
                                            } else {
                                                expanded = true
                                            }
                                        }
                                    )
                                },
                                trailingIcon = {
                                    AnimatedDotToXRotated(
                                        expanded = expanded,
                                        onClick = {
                                            dictionarySearchViewModel.clearSearchResults()
                                        }
                                    )
                                },
                            )
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        shape = SearchBarDefaults.inputFieldShape,
                    ) {
                        LazyColumn {
                            items(searchResults) { result ->
                                ListItem(
                                    headlineContent = {
                                        Column {
                                            Text(text = "Kanji: ${result.kanji}")
                                            Text(text = "Kana: ${result.kana}")
                                        }
                                    },
                                    trailingContent = { Text(text = " ${result.gloss}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun PreviewHomeScreen2() {
        HomeScreen2(navController = rememberNavController())
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AnimatedSearchToBackRotated(expanded: Boolean, onClick: () -> Unit) {
        val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
        val icon = if (rotation <= 90f) Icons.Filled.Search else Icons.AutoMirrored.Filled.ArrowForward

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .clickable(onClick = onClick)
                .graphicsLayer {
                    rotationZ = rotation
                    cameraDistance = 8 * density
                }
                .size(24.dp)
        )
    }

    @Composable
    fun AnimatedDotToXRotated(expanded: Boolean, onClick: () -> Unit) {
        val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
        val icon = if (rotation <= 90f) Icons.Filled.MoreVert else Icons.Filled.Close

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .clickable(onClick = onClick)
                .graphicsLayer {
                    rotationZ = rotation
                    cameraDistance = 8 * density
                }
                .size(24.dp)
        )
    }

    // ViewModel and Database Helper (You provided these in previous responses)
    // ...