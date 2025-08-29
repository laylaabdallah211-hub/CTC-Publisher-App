package com.example.ctcappactual.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ctcappactual.ui.theme.BluePrimary
import com.example.ctcappactual.ui.theme.OrangeAccent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerListScreen(navController: NavController) {
    val db = Firebase.firestore
    var trainers by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("trainers").get()
            .addOnSuccessListener { result ->
                trainers = result.documents.mapNotNull { it.data }
            }
    }

    // Background gradient for the whole screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BluePrimary,
                        Color.White,
                        OrangeAccent
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Trainers") },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = BluePrimary,
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = Color.Transparent // Make scaffold background transparent to show gradient
        ) { paddingValues ->

            LazyColumn(
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(trainers) { trainer ->
                    val name = trainer["name"] as? String ?: "N/A"
                    val bio = trainer["bio"] as? String ?: "N/A"
                    val qualifications = trainer["qualifications"] as? String ?: "N/A"
                    val rating = trainer["rating"]?.toString() ?: "N/A"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    "trainerDetail/$name/$bio/$qualifications/$rating"
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(2.dp, OrangeAccent),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: $name", style = MaterialTheme.typography.titleMedium, color = BluePrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Qualifications: $qualifications", color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Rating: $rating ⭐", color = OrangeAccent)
                        }
                    }
                }
            }
        }
    }
}




