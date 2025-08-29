package com.example.ctcappactual.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.ctcappactual.ui.theme.BluePrimary
import com.example.ctcappactual.ui.theme.OrangeAccent
import com.example.ctcappactual.ui.theme.White
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationsScreen(userId: String, onBack: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var notifications by remember { mutableStateOf<List<String>>(emptyList()) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = LocalDate.now()

    LaunchedEffect(Unit) {
        db.collection("registrations")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val notifList = mutableListOf<String>()
                for (doc in result.documents) {
                    val courseId = doc.getString("courseId") ?: continue

                    db.collection("courses").document(courseId).get()
                        .addOnSuccessListener { courseDoc ->
                            val title = courseDoc.getString("title") ?: "Untitled"
                            val startDateStr = courseDoc.getString("startDate")
                            if (startDateStr != null) {
                                try {
                                    val startDate = LocalDate.parse(startDateStr, formatter)
                                    val daysUntil = today.until(startDate).days
                                    if (daysUntil == 2) {
                                        notifList.add("📣 Your course \"$title\" starts in 2 days!")
                                        notifications = notifList.toList()
                                    }
                                } catch (_: Exception) {}
                            }
                        }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BluePrimary,
                        White,
                        OrangeAccent
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Notifications", color = BluePrimary) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BluePrimary)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = White)
                )
            },
            containerColor = Color.Transparent // keep gradient visible behind scaffold
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (notifications.isEmpty()) {
                    Text(
                        "No notifications yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = BluePrimary
                    )
                } else {
                    notifications.forEach { notification ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Text(
                                notification,
                                modifier = Modifier.padding(16.dp),
                                color = BluePrimary
                            )
                        }
                    }
                }
            }
        }
    }
}


