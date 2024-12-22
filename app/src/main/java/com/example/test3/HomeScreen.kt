package com.example.test3


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen() {
    val context = LocalContext.current // Get the current context
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.surface
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color(0xFF00BCD4),
//                                    Color(0xFF3F51B5)
//                                )
//                            )
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // App logo
//                    Image(
//                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
//                        contentDescription = "App Logo",
//                        modifier = Modifier
//                            .size(100.dp)
//                            .padding(bottom = 16.dp)
//                    )

                    // App name
                    Text(
                        text = "Immerse",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color =  MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(bottom = 50.dp)
                    )

                    // Add Link button
                    AnimatedButton(text = "Add Link", onClick = {
                        navController.navigate("addLinkScreen")
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    // Search Word button
                    AnimatedButton(text = "Search Word", onClick = {
//                        val intent = Intent(context, Search_Word::class.java)
//                        context.startActivity(intent)
                          //navController.navigate("SearchScreen")
                            navController.navigate("Learn")
                    })
                }
            }
        }
        composable("addLinkScreen") {
            AddLinkScreen()
        }
        composable("SearchScreen") {
            SearchWordScreen()
        }
        composable("Learn") {
            Learn()
        }
    }
}

@Composable
fun AnimatedButton(text: String, onClick: () -> Unit) {
    val scale by animateFloatAsState(targetValue = 1.0f, label = "")
    val elevation by animateDpAsState(targetValue = 4.dp, label = "")

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(0.8f)
            .height(50.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = elevation),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ),
//        colors = ButtonDefaults.buttonColors(
//            //backgroundColor = MaterialTheme.colorScheme.primary,
//            //contentColor = Color.White
//        ),
        contentPadding = PaddingValues()
    ) {
        Text(text = text)
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//public fun HomeAppBar(
//    //isExpanded: Boolean,
//    modifier: Modifier = Modifier.fillMaxWidth(),
//) {
//        SearchBar(
//            inputField = {
//                SearchBarDefaults.InputField(
//        ons.Default.MoreVert, contentDescription = null) },
//                )
//            },
//            query = "",
//            onQueryChange = {},
//            placeholder = {
//                Text("Search for a word")
//            },
//            onSearch = {},
//            active = false,
//            onActiveChange = {},
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = null
//                )
//            },
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.MoreVert,
//                    contentDescription = "Bruh"
//                )
//            },
//            //modifier = if (isExpanded) Modifier else Modifier.fillMaxWidth()
//        ) { }
//
//    Column(Modifier.verticalScroll(rememberScrollState())) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ){}
//        repeat(12) { idx ->
//            val resultText = "Suggestion $idx"
//            ListItem(
//                headlineContent = { Text(resultText) },
//                supportingContent = { Text("Additional info") },
//                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
//                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
//                modifier =
//                Modifier.clickable {
//                    //text = //resultText
//                    //expanded = false
//                }
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 4.dp)
//            )
//        }
//    }
//}

    @Preview(showBackground = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen()
    }