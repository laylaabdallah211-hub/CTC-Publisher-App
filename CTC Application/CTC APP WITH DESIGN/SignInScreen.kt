package com.example.ctcapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SignInScreen(
    onSignedIn: (String) -> Unit,
    onError: (String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Sign In", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val db = Firebase.firestore
            db.collection("users")
                .whereEqualTo("email", email.trim())
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val user = result.documents.first()
                        val savedPassword = user.getString("password")
                        if (savedPassword == password) {
                            onSignedIn(email.trim())
                        } else {
                            onError("Invalid password.")
                        }
                    } else {
                        onError("User not found.")
                    }
                }
                .addOnFailureListener {
                    onError("Error checking credentials.")
                }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Sign In")
        }
    }
}








