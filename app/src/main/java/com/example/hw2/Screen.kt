package com.example.hw2

sealed class Screen (val route: String){
    object MainScreen : Screen(route = "main_screen")
    object DetailScreen : Screen(route = "detail_screen")
    object MapScreen : Screen(route = "map_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
