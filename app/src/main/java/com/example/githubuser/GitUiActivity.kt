package com.example.githubuser

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.network.GithubRemoteDataSource
import com.example.network.RetrofitBuilder
import com.example.githubuser.ui.theme.GithubUserTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitUiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val context = applicationContext
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubUserTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GitUi(modifier = Modifier.padding(innerPadding), context = context)
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)

                }
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
    var userName by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }

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
            val show = Toast.makeText(context, userId, Toast.LENGTH_LONG).show()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = dataSource.getAvatarInfo(userId)
                    urlImage = response.url

                    CoroutineScope(Dispatchers.Main).launch {
                        userName = response.name ?: "No name available"
                        userBio = response.bio ?: "No bio available"
                    }
                } catch (e: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }) {
            Text(
                text = "GitHub User Info"
            )
        }

        if (urlImage.isNotEmpty()) {
            AsyncImage(
                model = urlImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(120.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .padding(8.dp)
            ) {
                Text(text = userId)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .padding(8.dp)
            ) {
                Text(text = userName)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .padding(8.dp)
                    .heightIn(min = 60.dp)
            ) {
                Text(text = userBio)
            }
        }
    }
}