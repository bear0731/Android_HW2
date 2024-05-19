package com.example.hw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.preview.Preview
import com.example.hw2.ui.theme.HW2Theme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                    Navigation()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HW2Theme {
        Navigation()
    }
}