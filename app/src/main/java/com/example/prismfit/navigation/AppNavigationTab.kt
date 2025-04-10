package com.example.prismfit.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.prismfit.R
import kotlinx.collections.immutable.persistentListOf

data class AppNavigationTab(
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    val graph: Any
)

val MainTabs = persistentListOf(
    AppNavigationTab(
        icon = Icons.Default.Home,
        labelRes = R.string.home_screen,
        graph = HomeGraph
    ),
    AppNavigationTab(
        icon = Icons.AutoMirrored.Filled.DirectionsRun,
        labelRes = R.string.activity_main_screen,
        graph = ActivityGraph
    ),
    AppNavigationTab(
        icon = Icons.Default.Restaurant,
        labelRes = R.string.diet_screen,
        graph = DietGraph
    ),
    AppNavigationTab(
        icon = Icons.Default.NoteAlt,
        labelRes = R.string.notes_screen,
        graph = NotesGraph
    )
)