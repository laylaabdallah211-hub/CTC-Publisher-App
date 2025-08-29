package com.example.ctcappactual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun RegistrationScreen(
    courseId: String,
    userId: String,
    onBack: () -> Unit,
    onRegistered: () -> Unit
) {
    val db = Firebase.firestore
    var course by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isRegistering by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(courseId) {
        db.collection("courses").document(courseId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    course = document.data
                }
                isLoading = false
            }
            .addOnFailureListener {
                errorMessage = "Failed to load course data."
                isLoading = false
            }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        course?.let { courseData ->
            Column(modifier = Modifier.padding(16.dp)) {
                val cost = (courseData["cost"] as? Number)?.toDouble() ?: 0.0
                val title = courseData["title"] as? String ?: "Unknown"
                val spotsLeft = (courseData["spotsLeft"] as? Number)?.toInt() ?: 0

                Text("Register for Course", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Course: $title")
                Text("Cost: $cost JD")
                Text("Spots Left: $spotsLeft")

                Spacer(modifier = Modifier.height(20.dp))

                successMessage?.let { msg ->
                    Text(msg, color = MaterialTheme.colorScheme.primary)
                }

                errorMessage?.let { msg ->
                    Text(msg, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = {
                        if (spotsLeft <= 0) {
                            errorMessage = "No spots left for this course."
                            return@Button
                        }

                        isRegistering = true

                        val registration = hashMapOf(
                            "userId" to userId,
                            "courseId" to courseId,
                            "timestamp" to System.currentTimeMillis(),
                            "cost" to cost
                        )

                        db.collection("registrations").add(registration)
                            .addOnSuccessListener {
                                db.collection("paymentData").document("balance")
                                    .update("total", FieldValue.increment(cost))
                                    .addOnSuccessListener {
                                        db.collection("courseStats").document(courseId) // ✅ FIXED HERE
                                            .set(
                                                mapOf(
                                                    "courseTitle" to title,
                                                    "registrations" to FieldValue.increment(1),
                                                    "totalRevenue" to FieldValue.increment(cost),
                                                    "profit" to FieldValue.increment(cost * 0.1)
                                                ),
                                                SetOptions.merge()
                                            )
                                            .addOnSuccessListener {
                                                db.collection("courses").document(courseId)
                                                    .update("spotsLeft", FieldValue.increment(-1))
                                                    .addOnSuccessListener {
                                                        successMessage = "Registration successful ✅"
                                                        isRegistering = false
                                                        onRegistered()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        errorMessage = "Failed to update spots: ${e.message}"
                                                        isRegistering = false
                                                    }
                                            }
                                            .addOnFailureListener { e ->
                                                errorMessage = "Failed to update stats: ${e.message}"
                                                isRegistering = false
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        errorMessage = "Failed to update balance: ${e.message}"
                                        isRegistering = false
                                    }
                            }
                            .addOnFailureListener { e ->
                                errorMessage = "Registration failed: ${e.message}"
                                isRegistering = false
                            }
                    },
                    enabled = !isRegistering,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pay & Register")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Back")
                }
            }
        } ?: Text("Course not found.")
    }
}


















