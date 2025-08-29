package com.example.ctcappactual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ViewProfileScreen(email: String, onBack: () -> Unit) {
    val db = Firebase.firestore

    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var degree by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }

    var userDocId by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(email) {
        db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener { result ->
            if (!result.isEmpty) {
                val doc = result.documents.first()
                userDocId = doc.id

                password = doc.getString("password") ?: ""
                name = doc.getString("name") ?: ""
                phone = doc.getString("phone") ?: ""
                gender = doc.getString("gender") ?: ""
                dob = doc.getString("dob") ?: ""
                occupation = doc.getString("occupation") ?: ""
                degree = doc.getString("degree") ?: ""
                interests = doc.getString("interests") ?: ""
            }
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Your Profile", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = occupation, onValueChange = { occupation = it }, label = { Text("Occupation") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = degree, onValueChange = { degree = it }, label = { Text("Highest Degree") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = interests, onValueChange = { interests = it }, label = { Text("Fields of Interest") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val updated = mapOf(
                    "password" to password,
                    "name" to name,
                    "phone" to phone,
                    "gender" to gender,
                    "dob" to dob,
                    "occupation" to occupation,
                    "degree" to degree,
                    "interests" to interests
                )
                db.collection("users").document(userDocId).update(updated)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        }
    }
}

