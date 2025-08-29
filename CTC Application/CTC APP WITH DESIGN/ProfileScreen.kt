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
fun ProfileScreen(
    signedInEmail: String? = null,
    onSave: (String) -> Unit
) {
    val db = Firebase.firestore

    var email by remember { mutableStateOf(signedInEmail ?: "") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var degree by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Sign Up", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
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
            val userData = hashMapOf(
                "email" to email.trim(),
                "password" to password,
                "name" to name,
                "phone" to phone,
                "gender" to gender,
                "dob" to dob,
                "occupation" to occupation,
                "degree" to degree,
                "interests" to interests
            )
            db.collection("users").add(userData).addOnSuccessListener {
                onSave(email.trim())
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}












