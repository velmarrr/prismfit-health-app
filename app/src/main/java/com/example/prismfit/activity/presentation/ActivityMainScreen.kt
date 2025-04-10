package com.example.prismfit.activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.prismfit.navigation.ActivityGraph
import com.example.prismfit.navigation.ActivityGraph.PendingActivityRoute
import com.example.prismfit.navigation.LocalNavController

@Composable
fun ActivityMainScreen() {
    val navController = LocalNavController.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Activity Main Screen",
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Button(
            onClick = { navController.navigate(PendingActivityRoute) }
        ) {
            Text("Start activity")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ActivityMainScreenPreview() {
    ActivityMainScreen()
}