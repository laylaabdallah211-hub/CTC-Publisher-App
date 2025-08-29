package com.example.ctcappactual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TrainerDetailScreen(
    name: String,
    bio: String,
    qualifications: String,
    rating: Double
) {
    val formattedRating = String.format("%.1f", rating)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Name: $name", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Bio: $bio")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Qualifications: $qualifications")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Rating: $formattedRating ⭐")
    }
}



