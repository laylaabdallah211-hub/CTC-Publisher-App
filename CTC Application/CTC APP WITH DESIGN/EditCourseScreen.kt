package com.example.ctcappactual.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ctcappactual.model.Course
import com.example.ctcappactual.model.CourseFormState
import com.example.ctcappactual.ui.theme.BluePrimary
import com.example.ctcappactual.ui.theme.OrangeAccent
import kotlinx.coroutines.launch

@Composable
fun EditCourseScreen(
    course: Course,
    onUpdateCourse: (CourseFormState, () -> Unit) -> Unit,
    onDeleteCourse: (Course, () -> Unit) -> Unit,
    onCancel: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf(course.title) }
    var trainer by remember { mutableStateOf(course.trainer) }
    var qualifications by remember { mutableStateOf(course.qualifications) }
    var place by remember { mutableStateOf(course.place) }
    var theme by remember { mutableStateOf(course.theme) }
    var startDate by remember { mutableStateOf(course.startDate) }
    var endDate by remember { mutableStateOf(course.endDate) }
    var duration by remember { mutableStateOf(course.duration.toString()) }
    var maxSpots by remember { mutableStateOf(course.maxSpots.toString()) }
    var spotsLeft by remember { mutableStateOf(course.spotsLeft.toString()) }
    var cost by remember { mutableStateOf(course.cost.toString()) }
    var details by remember { mutableStateOf(course.details) }
    var requirements by remember { mutableStateOf(course.requirements) }
    var partyInitiating by remember { mutableStateOf(course.partyInitiating) }

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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Edit Course: ${course.title}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                @Composable
                fun field(
                    label: String,
                    value: String,
                    onChange: (String) -> Unit,
                    keyboardType: KeyboardType = KeyboardType.Text
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onChange,
                        label = { Text(label) },
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedTextField(
                    value = course.id,
                    onValueChange = {},
                    label = { Text("Course ID (read-only)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(8.dp))

                field("Course Title", title, { title = it })
                field("Trainer Name", trainer, { trainer = it })
                field("Qualifications", qualifications, { qualifications = it })
                field("Place", place, { place = it })
                field("Theme", theme, { theme = it })
                field("Start Date (YYYY-MM-DD)", startDate, { startDate = it })
                field("End Date (YYYY-MM-DD)", endDate, { endDate = it })
                field("Duration (hours)", duration, { duration = it }, KeyboardType.Number)
                field("Max Spots", maxSpots, { maxSpots = it }, KeyboardType.Number)
                field("Spots Left", spotsLeft, { spotsLeft = it }, KeyboardType.Number)
                field("Cost (JD)", cost, { cost = it }, KeyboardType.Number)
                field("Details", details, { details = it })
                field("Requirements", requirements, { requirements = it })
                field("Party Initiating", partyInitiating, { partyInitiating = it })

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            val form = CourseFormState(
                                id = course.id,
                                title = title,
                                trainer = trainer,
                                qualifications = qualifications,
                                place = place,
                                theme = theme,
                                startDate = startDate,
                                endDate = endDate,
                                duration = duration.toIntOrNull() ?: 0,
                                maxSpots = maxSpots.toIntOrNull() ?: 0,
                                spotsLeft = spotsLeft.toIntOrNull() ?: 0,
                                cost = cost.toDoubleOrNull() ?: 0.0,
                                details = details,
                                requirements = requirements,
                                partyInitiating = partyInitiating,
                                publisher = course.publisher
                            )
                            onUpdateCourse(form) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("✅ Course updated")
                                }
                            }
                        }
                    ) {
                        Text("Update Course")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedButton(
                        onClick = {
                            onDeleteCourse(course) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("❌ Course deleted")
                                }
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Delete")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}











