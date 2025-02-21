package com.example.user

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.network.GithubRemoteDataSource
import com.example.network.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val context = applicationContext
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Usa tu tema aquí
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                GitUi(modifier = Modifier.padding(innerPadding), context = context)
            }
        }
    }
}

@Composable
fun GitUi(modifier: Modifier = Modifier, context: Context) {
    val dataSource: GithubRemoteDataSource = GithubRemoteDataSource(
        RetrofitBuilder
    )
    var urlImage by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GitHub User Info"
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            value = userId,
            onValueChange = {
                userId = it
            }
        )
        Button(onClick = {
            Toast.makeText(context, userId, Toast.LENGTH_LONG).show()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = dataSource.getAvatarInfo(userId)
                    urlImage = response.url
                } catch (e: Exception) {
                    // Manejar el error
                }
            }
        }) {
            Text(text = "Buscar Usuario")
        }
        AsyncImage(
            model = urlImage,
            contentDescription = null
        )
    }
}