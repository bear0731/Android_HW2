package com.example.hw2

import android.util.Log
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.log

data class Landmark(val id: Int, val name: String, val image: Int, val description: String, val latitude: Double, val longitude: Double)

// List of landmarks
val landmarks = listOf(
    Landmark(1,"Golden Gate Bridge", R.drawable.golden_gate_bridge, "The Golden Gate Bridge is a suspension bridge spanning the Golden Gate, the one-mile-wide strait connecting San Francisco Bay and the Pacific Ocean.", 37.8199, -122.4783),
    Landmark(2,"Statue of Liberty", R.drawable.statue_of_liberty, "The Statue of Liberty is a copper statue, a gift from the people of France to the people of the United States, and a welcoming symbol to immigrants arriving in the New World.", 40.6892, -74.0445),
    Landmark(3,"Eiffel Tower", R.drawable.eiffel_tower, "The Eiffel Tower is a wrought iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower.", 48.8584, 2.2945),
    Landmark(4,"Great Wall of China", R.drawable.great_wall_of_china, "The Great Wall of China is a series of fortifications made of stone, brick, tamped earth, wood, and metal, built in China as a defensive measure against various nomadic groups of the Eurasian Steppe.", 40.4319, 116.5704),
    Landmark(5,"Taj Mahal", R.drawable.taj_mahal, "The Taj Mahal is an ivory-white marble mausoleum on the south bank of the Yamuna river in the Indian city of Agra. It was commissioned in 1631 by the Mughal emperor Shah Jahan in memory of his wife Mumtaz Mahal.", 27.1751, 78.0421)
)

@Composable
fun Navigation() {
    val navigation = rememberNavController()
    NavHost(navController = navigation, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {
            MainScreen(navController = navigation)
        }
        composable(
            route = Screen.DetailScreen.route+"/{landmarkId}",
            arguments = listOf(
                navArgument("landmarkId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        )
        {
            DetailScreen(navController = navigation, landmarkId = it.arguments?.getInt("landmarkId"))
        }
        composable(
            route = Screen.MapScreen.route+"/{landmarkId}",
            arguments = listOf(
            navArgument("landmarkId") {
                type = NavType.IntType
                defaultValue = 1
                }
            )
        ){
            Map(navController = navigation,landmarkId = it.arguments?.getInt("landmarkId"))
        }
    }
}
@Composable
fun MainScreen(navController: NavController) {
    LazyColumn {
        items(landmarks) { landmark ->
            LandmarkCard(landmark, navController)
        }
    }
}

@Composable
fun LandmarkCard(landmark: Landmark, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                Log.v("TAG", "LandmarkCard: ${landmark.id}")
                navController.navigate(Screen.DetailScreen.withArgs(landmark.id.toString())) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = landmark.image),
                contentDescription = landmark.name,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = landmark.name)
            Text(text = landmark.description)
        }
    }
}

@Composable
fun DetailScreen(navController: NavController, landmarkId: Int?) {
    val landmark = landmarks.find { it.id == landmarkId }
    Log.v("TAG", "LandmarkCard in detail: ${landmark!!.id}")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { navController.popBackStack() }, // Add a back button
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Back")
        }
        Image(
            painter = painterResource(id = landmark!!.image),
            contentDescription = landmark.name,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(text = landmark.name)
        Text(text = landmark.description)
        Button(
            onClick = { navController.navigate(Screen.MapScreen.withArgs(landmark.id.toString())) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(text = "Show on GoogleMap")
        }
    }
}

@Composable
fun Map(navController: NavController,landmarkId: Int?) {
    val landmark = landmarks.find { it.id == landmarkId }
    val lat = landmark!!.latitude
    val lon = landmark!!.longitude
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { navController.popBackStack() }, // Add a back button
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Back")
        }
        val location = LatLng(lat, lon)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(location, 15f)
        }
        Box(modifier = Modifier.fillMaxSize()){
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ){
                Marker(
                    state = MarkerState(position = location),
                    title = landmark!!.name
                )
            }
        }

    }

}
