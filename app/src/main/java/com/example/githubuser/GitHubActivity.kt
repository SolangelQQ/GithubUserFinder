package com.example.githubuser

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tudominio.network.GithubRemoteDataSource
import com.tudominio.network.RetrofitBuilder
import com.tudominio.app.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GitHubActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold { innerPadding ->
                        GitHubScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun GitHubScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dataSource = GithubRemoteDataSource(RetrofitBuilder)

    var urlImage by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GitHub Profile Viewer",
            style = MaterialTheme.typography.headlineMedium
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = userId,
            onValueChange = { userId = it },
            label = { Text("Enter GitHub Username") },
            singleLine = true
        )

        Button(
            onClick = {
                if (userId.isBlank()) {
                    Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                errorMessage = ""

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = dataSource.getAvatarInfo(userId)

                        withContext(Dispatchers.Main) {
                            urlImage = response.url
                            userName = response.login
                            isLoading = false
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage = "Error: ${e.localizedMessage ?: "Unknown error"}"
                            isLoading = false
                        }
                    }
                }
            }
        ) {
            Text(text = "Search")
        }

        if (isLoading) {
            Text("Loading...")
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (userName.isNotEmpty()) {
            Text(
                text = "Username: $userName",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (urlImage.isNotEmpty()) {
            AsyncImage(
                model = urlImage,
                contentDescription = "GitHub Avatar for $userName",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(0.7f)
            )
        }
    }
}