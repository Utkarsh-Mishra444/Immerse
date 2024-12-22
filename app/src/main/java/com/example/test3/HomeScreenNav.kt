package com.example.test3


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenNav() {
    val context = LocalContext.current // Get the current context
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen2(navController)
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


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenNav() {
    HomeScreenNav()
}