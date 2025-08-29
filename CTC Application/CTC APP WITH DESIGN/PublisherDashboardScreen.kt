package com.example.ctcappactual.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ctcappactual.model.Course

@Composable
fun PublisherDashboardScreen(
    courses: List<Course>,
    onCreateNewCourseClick: () -> Unit,
    onEditCourseClick: (Course) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("👋 Welcome, Admin!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onCreateNewCourseClick, modifier = Modifier.fillMaxWidth()) {
            Text("Create New Course")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("📚 Your Published Courses:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (courses.isEmpty()) {
            Text("You haven’t published any courses yet.")
        } else {
            LazyColumn {
                items(courses) { course ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEditCourseClick(course) }
                            .padding(12.dp)
                    ) {
                        Text("• ${course.title}", style = MaterialTheme.typography.titleMedium)
                        Text("Trainer: ${course.trainer}", style = MaterialTheme.typography.bodyMedium)
                        Text("Start: ${course.startDate} — End: ${course.endDate}", style = MaterialTheme.typography.bodySmall)
                    }
                    Divider()
                }
            }
        }
    }
}

