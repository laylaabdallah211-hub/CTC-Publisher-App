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
import com.example.ctcappactual.model.CourseFormState
import com.example.ctcappactual.ui.theme.BluePrimary
import com.example.ctcappactual.ui.theme.OrangeAccent
import kotlinx.coroutines.launch

@Composable
fun CreateCourseScreen(
    publisherEmail: String,
    onCreateCourse: (CourseFormState, () -> Unit) -> Unit,
    onCancel: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var trainer by remember { mutableStateOf("") }
    var qualifications by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var maxSpots by remember { mutableStateOf("") }
    var spotsLeft by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var theme by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    var partyInitiating by remember { mutableStateOf("") }

    // Background gradient layer
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
                Text("Create New Course", style = MaterialTheme.typography.headlineSmall)
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

                field("Course ID", id, { id = it })
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
                                id = id,
                                title = title,
                                trainer = trainer,
                                qualifications = qualifications,
                                place = place,
                                startDate = startDate,
                                endDate = endDate,
                                duration = duration.toIntOrNull() ?: 0,
                                maxSpots = maxSpots.toIntOrNull() ?: 0,
                                spotsLeft = spotsLeft.toIntOrNull() ?: 0,
                                cost = cost.toDoubleOrNull() ?: 0.0,
                                theme = theme,
                                details = details,
                                requirements = requirements,
                                partyInitiating = partyInitiating,
                                publisher = publisherEmail
                            )
                            onCreateCourse(form) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("✅ Course created!")
                                }
                            }
                        }
                    ) {
                        Text("Save")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedButton(
                        onClick = onCancel,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}









