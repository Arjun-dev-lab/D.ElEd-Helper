package com.askproductions.deledhelper

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askproductions.deledhelper.ui.theme.DElEdHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DElEdHelperTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        MainScreen(Modifier.padding(innerPadding))
                    }
                )
            }
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(this, WebViewActivity::class.java).apply {
            putExtra("url", url)
        }
        startActivity(intent)
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { openWebsite("https://arjun-dev-lab.github.io/EduVerseApps/malayalam%20d.el.ed/malayalam-d.el.ed.html") }) {
                Text("Open Website 1")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { openWebsite("https://arjun-dev-lab.github.io/EduVerseApps/malayalam%20d.el.ed/school%20resource/resource.html") }) {
                Text("Open Website 2")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { openWebsite("https://arjun-dev-lab.github.io/EduVerseApps/malayalam%20d.el.ed/HandBooks/handbook.html") }) {
                Text("Open Website 3")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { openWebsite("https://www.google.com") }) {
                Text("Open Website 4")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DElEdHelperTheme {
        MainActivity().MainScreen()
    }
}
