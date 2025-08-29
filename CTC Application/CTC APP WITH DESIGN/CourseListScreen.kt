package com.example.ctcappactual.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun CourseListScreen(
    showSignButtons: Boolean,
    onSignUp: () -> Unit,
    onSignIn: () -> Unit,
    onCourseClick: (String) -> Unit,
    onTrainerClick: () -> Unit,
    onLogout: () -> Unit,
    onViewProfile: () -> Unit,
    isAdmin: Boolean,
    onAdminReports: () -> Unit,
    onPublisherDashboard: () -> Unit,
    onNotifications: () -> Unit
) {
    val db = Firebase.firestore
    var allCourses by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var filteredCourses by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    var themeFilter by remember { mutableStateOf("") }
    var trainerFilter by remember { mutableStateOf("") }
    var minDurationFilter by remember { mutableStateOf("") }

    fun applyFilters() {
        filteredCourses = allCourses.filter { course ->
            val theme = (course["theme"] ?: "").toString().lowercase()
            val trainer = (course["trainer"] ?: "").toString().lowercase()
            val duration = (course["duration"] as? Number)?.toInt() ?: 0
            val minDuration = minDurationFilter.toIntOrNull() ?: 0

            theme.contains(themeFilter.lowercase()) &&
                    trainer.contains(trainerFilter.lowercase()) &&
                    duration >= minDuration
        }
    }

    LaunchedEffect(Unit) {
        db.collection("courses").get().addOnSuccessListener { result ->
            val courses = result.documents.mapNotNull {
                val data = it.data?.toMutableMap()
                data?.put("id", it.id)
                data
            }
            allCourses = courses
            filteredCourses = courses
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent // Make surface transparent to let gradient show
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Filters
            item {
                Text("Upcoming Courses", style = MaterialTheme.typography.titleLarge)
            }

            item {
                OutlinedTextField(
                    value = themeFilter,
                    onValueChange = {
                        themeFilter = it
                        applyFilters()
                    },
                    label = { Text("Filter by Theme") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = trainerFilter,
                    onValueChange = {
                        trainerFilter = it
                        applyFilters()
                    },
                    label = { Text("Filter by Trainer") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = minDurationFilter,
                    onValueChange = {
                        minDurationFilter = it
                        applyFilters()
                    },
                    label = { Text("Minimum Duration (hrs)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Courses list
            items(filteredCourses) { course ->
                val courseId = course["id"].toString()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCourseClick(courseId) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Title: ${course["title"] ?: "N/A"}", style = MaterialTheme.typography.titleMedium)
                        Text("Trainer: ${course["trainer"] ?: "N/A"}")
                    }
                }
            }

            // Buttons
            item {
                Button(onClick = onTrainerClick, modifier = Modifier.fillMaxWidth()) {
                    Text("View Trainers")
                }
            }

            item {
                if (showSignButtons) {
                    Row {
                        Button(onClick = onSignIn, modifier = Modifier.weight(1f)) {
                            Text("Sign In")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onSignUp, modifier = Modifier.weight(1f)) {
                            Text("Sign Up")
                        }
                    }
                } else {
                    Column {
                        Button(onClick = onViewProfile, modifier = Modifier.fillMaxWidth()) {
                            Text("View Profile")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = onNotifications, modifier = Modifier.fillMaxWidth()) {
                            Text("Notifications")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isAdmin) {
                            Button(onClick = onAdminReports, modifier = Modifier.fillMaxWidth()) {
                                Text("View Admin Reports")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(onClick = onPublisherDashboard, modifier = Modifier.fillMaxWidth()) {
                                Text("Publisher Dashboard")
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = onLogout,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Logout", color = MaterialTheme.colorScheme.onError)
                        }
                    }
                }
            }
        }
    }
}



























