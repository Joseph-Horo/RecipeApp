package com.example.recipeapp

import dagger.hilt.android.AndroidEntryPoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipeapp.navigation.NavigationSubGraphs
import com.example.recipeapp.navigation.RecipeNavigation
import com.example.recipeapp.ui.theme.RecipeAppTheme
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationSubGraphs: NavigationSubGraphs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                Surface (modifier = Modifier.safeContentPadding()){
                    RecipeNavigation(navigationSubGraphs = navigationSubGraphs)

                }
            }
        }
    }
}

