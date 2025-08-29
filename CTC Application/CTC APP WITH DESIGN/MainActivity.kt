package com.example.ctcappactual

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ctcappactual.ui.theme.BluePrimary
import com.example.ctcappactual.ui.theme.CtcappactualTheme
import com.example.ctcappactual.ui.theme.OrangeAccent
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ctcappactual.screens.*
import com.example.ctcapplication.SignInScreen
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import com.example.ctcappactual.viewmodel.CourseViewModel
import com.example.ctcappactual.model.Course
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    companion object {
        const val ADMIN_EMAIL = "admin@example.com"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CtcappactualTheme { // Wrap in your custom theme

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                var isSignedIn by remember { mutableStateOf(false) }
                var isAdmin by remember { mutableStateOf(false) }
                var signedInEmail by remember { mutableStateOf("") }

                val currentUserId = signedInEmail

                // State to store the course to edit safely
                var courseToEdit by remember { mutableStateOf<Course?>(null) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    BluePrimary,
                                    MaterialTheme.colorScheme.background,
                                    OrangeAccent
                                )
                            )
                        )
                ) {


                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent, // Important: make Scaffold background transparent
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { padding ->
                        NavHost(
                            navController = navController,
                            startDestination = "courseList",
                            modifier = Modifier.padding(padding)
                        ) {
                            composable("courseList") {
                                CourseListScreen(
                                    showSignButtons = !isSignedIn,
                                    onSignUp = { navController.navigate("profile") },
                                    onSignIn = { navController.navigate("signIn") },
                                    onCourseClick = { id -> navController.navigate("courseDetails/$id") },
                                    onTrainerClick = { navController.navigate("trainerList") },
                                    onLogout = {
                                        isSignedIn = false
                                        signedInEmail = ""
                                        isAdmin = false
                                        navController.navigate("courseList") {
                                            popUpTo("courseList") { inclusive = true }
                                        }
                                    },
                                    onViewProfile = { navController.navigate("viewProfile") },
                                    isAdmin = isAdmin,
                                    onAdminReports = { navController.navigate("adminReports") },
                                    onPublisherDashboard = { navController.navigate("publisherDashboard") },
                                    onNotifications = { navController.navigate("notifications") }
                                )
                            }

                            composable("signIn") {
                                SignInScreen(
                                    onSignedIn = { email ->
                                        isSignedIn = true
                                        signedInEmail = email
                                        isAdmin = email == ADMIN_EMAIL
                                        navController.navigate("courseList") {
                                            popUpTo("courseList") { inclusive = true }
                                        }
                                    },
                                    onError = {
                                        scope.launch { snackbarHostState.showSnackbar(it) }
                                    }
                                )
                            }

                            composable("profile") {
                                ProfileScreen(
                                    signedInEmail = if (isSignedIn) signedInEmail else null,
                                    onSave = { email ->
                                        isSignedIn = true
                                        signedInEmail = email
                                        isAdmin = email == ADMIN_EMAIL
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Signed up successfully!")
                                        }
                                        navController.navigate("courseList") {
                                            popUpTo("courseList") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("viewProfile") {
                                ViewProfileScreen(email = signedInEmail) {
                                    navController.popBackStack()
                                }
                            }

                            composable(
                                "courseDetails/{courseId}",
                                arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                            ) { backStack ->
                                val courseId = backStack.arguments?.getString("courseId") ?: ""
                                CourseDetailsScreen(
                                    courseId = courseId,
                                    onBack = { navController.popBackStack() },
                                    navController = navController,
                                    userId = currentUserId,
                                    isSignedIn = isSignedIn
                                )
                            }

                            composable(
                                "registration/{courseId}",
                                arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                            ) { backStack ->
                                val courseId = backStack.arguments?.getString("courseId") ?: ""
                                RegistrationScreen(
                                    courseId = courseId,
                                    userId = currentUserId,
                                    onBack = { navController.popBackStack() },
                                    onRegistered = {
                                        navController.popBackStack()
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Registered successfully!")
                                        }
                                    }
                                )
                            }

                            composable("notifications") {
                                NotificationsScreen(userId = currentUserId) {
                                    navController.popBackStack()
                                }
                            }

                            composable("trainerList") {
                                TrainerListScreen(navController)
                            }

                            composable(
                                "trainerDetail/{name}/{bio}/{qualifications}/{rating}",
                                arguments = listOf(
                                    navArgument("name") { type = NavType.StringType },
                                    navArgument("bio") { type = NavType.StringType },
                                    navArgument("qualifications") { type = NavType.StringType },
                                    navArgument("rating") { type = NavType.FloatType }
                                )
                            ) { backStack ->
                                TrainerDetailScreen(
                                    name = backStack.arguments?.getString("name") ?: "",
                                    bio = backStack.arguments?.getString("bio") ?: "",
                                    qualifications = backStack.arguments?.getString("qualifications") ?: "",
                                    rating = backStack.arguments?.getFloat("rating")?.toDouble() ?: 0.0
                                )
                            }

                            if (isAdmin) {
                                composable("adminReports") {
                                    AdminReportsScreen(onBack = { navController.popBackStack() })
                                }

                                composable("publisherDashboard") {
                                    val viewModel = remember { CourseViewModel() }
                                    val courses by viewModel.courses.collectAsState()
                                    val publishedCourses = courses.filter {
                                        it.publisher == signedInEmail
                                    }

                                    PublisherDashboardScreen(
                                        courses = publishedCourses,
                                        onCreateNewCourseClick = {
                                            navController.navigate("createCourse")
                                        },
                                        onEditCourseClick = { course ->
                                            courseToEdit = course
                                            navController.navigate("editCourse")
                                        }
                                    )
                                }

                                composable("createCourse") {
                                    CreateCourseScreen(
                                        publisherEmail = signedInEmail,
                                        onCreateCourse = { form, onSuccess ->
                                            val viewModel = CourseViewModel()
                                            viewModel.addCourse(form) { success ->
                                                if (success) {
                                                    onSuccess()
                                                    navController.popBackStack()
                                                }
                                            }
                                        },
                                        onCancel = { navController.popBackStack() }
                                    )
                                }

                                composable("editCourse") {
                                    courseToEdit?.let { course ->
                                        EditCourseScreen(
                                            course = course,
                                            onUpdateCourse = { updatedForm, onSuccess ->
                                                val viewModel = CourseViewModel()
                                                viewModel.updateCourse(course.id, updatedForm) { success ->
                                                    if (success) {
                                                        onSuccess()
                                                        navController.popBackStack()
                                                    }
                                                }
                                            },
                                            onDeleteCourse = { deletingCourse, onSuccess ->
                                                val viewModel = CourseViewModel()
                                                viewModel.deleteCourse(deletingCourse.id) { success ->
                                                    if (success) {
                                                        onSuccess()
                                                        navController.popBackStack()
                                                    }
                                                }
                                            },
                                            onCancel = { navController.popBackStack() }
                                        )
                                    } ?: Text("⚠️ Error: No course loaded.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


























