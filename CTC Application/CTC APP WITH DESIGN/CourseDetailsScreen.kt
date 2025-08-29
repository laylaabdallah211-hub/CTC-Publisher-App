package com.example.ctcappactual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.Alignment

@Composable
fun CourseDetailsScreen(
    courseId: String,
    onBack: () -> Unit,
    navController: NavController,
    userId: String,
    isSignedIn: Boolean
) {
    val db = Firebase.firestore
    var course by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(courseId) {
        db.collection("courses").document(courseId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    course = document.data
                }
                isLoading = false
            }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        course?.let {
            val maxSpots = (it["maxSpots"] as? Number)?.toInt() ?: 0
            val spotsLeft = (it["spotsLeft"] as? Number)?.toInt() ?: 0

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Course Details", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Title: ${it["title"] ?: "N/A"}")
                Text("Trainer: ${it["trainer"] ?: "N/A"}")
                Text("Start Date: ${it["startDate"] ?: "N/A"}")
                Text("End Date: ${it["endDate"] ?: "N/A"}")
                Text("Place: ${it["place"] ?: "N/A"}")
                Text("Cost: ${it["cost"] ?: "N/A"}")
                Text("Details: ${it["details"] ?: "N/A"}")
                Text("Requirements: ${it["requirements"] ?: "N/A"}")
                Text("Spots Left: $spotsLeft / $maxSpots")

                Spacer(modifier = Modifier.height(16.dp))

                if (isSignedIn) {
                    if (spotsLeft > 0) {
                        Button(onClick = {
                            navController.navigate("registration/$courseId")
                        }) {
                            Text("Register")
                        }
                    } else {
                        Text("Course is full.", color = MaterialTheme.colorScheme.error)
                    }
                } else {
                    Text("You must be signed in to register.", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text("Back")
                }
            }
        } ?: run {
            Text("Course not found or failed to load.")
        }
    }
}












