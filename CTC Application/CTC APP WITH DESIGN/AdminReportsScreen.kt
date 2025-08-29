package com.example.ctcappactual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class ReportEntry(
    val title: String,
    val maxSpots: Int,
    val spotsLeft: Int,
    val registrations: Int,
    val revenue: Double,
    val profit: Double
)

@Composable
fun AdminReportsScreen(onBack: () -> Unit) {
    val db = Firebase.firestore
    var reportList by remember { mutableStateOf<List<ReportEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var totalBalance by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        val courseMap = mutableMapOf<String, Triple<String, Int, Int>>() // courseId → (title, maxSpots, spotsLeft)

        db.collection("courses").get().addOnSuccessListener { courses ->
            for (doc in courses.documents) {
                val id = doc.id
                val title = doc.getString("title") ?: continue
                val maxSpots = (doc.getLong("maxSpots") ?: 0L).toInt()
                val spotsLeft = (doc.getLong("spotsLeft") ?: 0L).toInt()
                courseMap[id] = Triple(title, maxSpots, spotsLeft)
            }

            db.collection("courseStats").get().addOnSuccessListener { statsSnapshot ->
                val reports = mutableListOf<ReportEntry>()
                var balanceSum = 0.0

                for (doc in statsSnapshot.documents) {
                    val courseId = doc.id
                    val courseData = courseMap[courseId] ?: continue
                    val (title, maxSpots, spotsLeft) = courseData

                    val registrations = (doc.getLong("registrations") ?: 0L).toInt()
                    val revenue = doc.getDouble("totalRevenue") ?: 0.0
                    val profit = doc.getDouble("profit") ?: 0.0

                    balanceSum += revenue

                    reports.add(
                        ReportEntry(
                            title = title,
                            maxSpots = maxSpots,
                            spotsLeft = spotsLeft,
                            registrations = registrations,
                            revenue = revenue,
                            profit = profit
                        )
                    )
                }

                reportList = reports
                totalBalance = balanceSum
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // 👋 Greeting
        Text("👋 Hello Admin!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Text("💰 Total Balance: ${"%.2f".format(totalBalance)} JD", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(reportList) { report ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Course: ${report.title}", style = MaterialTheme.typography.titleMedium)
                            Text("Max Spots: ${report.maxSpots}")
                            Text("Spots Left: ${report.spotsLeft}")
                            Text("Registered: ${report.registrations}")
                            Text("Balance: ${"%.2f".format(report.revenue)} JD")
                            Text("Profit (10%): ${"%.2f".format(report.profit)} JD")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        }
    }
}



