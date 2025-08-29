package com.example.ctcappactual.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ctcappactual.model.Course
import com.example.ctcappactual.model.CourseFormState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CourseViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    init {
        fetchCourses()
    }

    fun fetchCourses() {
        db.collection("courses").get().addOnSuccessListener { result ->
            val courseList = result.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Course::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    null
                }
            }
            _courses.value = courseList
        }
    }

    fun addCourse(courseForm: CourseFormState, onComplete: (Boolean) -> Unit) {
        val course = Course(
            id = courseForm.id,
            title = courseForm.title,
            trainer = courseForm.trainer,
            qualifications = courseForm.qualifications,
            place = courseForm.place,
            startDate = courseForm.startDate,
            endDate = courseForm.endDate,
            duration = courseForm.duration,
            maxSpots = courseForm.maxSpots,
            spotsLeft = courseForm.spotsLeft,
            cost = courseForm.cost,
            theme = courseForm.theme,
            details = courseForm.details,
            requirements = courseForm.requirements,
            partyInitiating = courseForm.partyInitiating,
            publisher = courseForm.publisher
        )
        db.collection("courses").document(course.id).set(course)
            .addOnSuccessListener { fetchCourses(); onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateCourse(courseId: String, updatedForm: CourseFormState, onComplete: (Boolean) -> Unit) {
        val updatedCourse = Course(
            id = updatedForm.id,
            title = updatedForm.title,
            trainer = updatedForm.trainer,
            qualifications = updatedForm.qualifications,
            place = updatedForm.place,
            startDate = updatedForm.startDate,
            endDate = updatedForm.endDate,
            duration = updatedForm.duration,
            maxSpots = updatedForm.maxSpots,
            spotsLeft = updatedForm.spotsLeft,
            cost = updatedForm.cost,
            theme = updatedForm.theme,
            details = updatedForm.details,
            requirements = updatedForm.requirements,
            partyInitiating = updatedForm.partyInitiating,
            publisher = updatedForm.publisher
        )
        db.collection("courses").document(courseId).set(updatedCourse)
            .addOnSuccessListener { fetchCourses(); onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteCourse(courseId: String, onComplete: (Boolean) -> Unit) {
        db.collection("courses").document(courseId).delete()
            .addOnSuccessListener { fetchCourses(); onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}








