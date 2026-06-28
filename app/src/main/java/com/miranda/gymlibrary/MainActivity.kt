package com.miranda.gymlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.miranda.gymlibrary.core.ui.theme.GymLibraryTheme
import com.miranda.gymlibrary.presentation.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymLibraryTheme {
                NavGraph()
            }
        }
    }
}
