package com.example.prismfit.core.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.prismfit.R
import com.example.prismfit.navigation.SettingsGraph.SettingsRoute
import com.example.prismfit.navigation.routeClass

sealed class NavigateUpAction {
    data object Hidden : NavigateUpAction()
    data class Visible(
        val onClick: () -> Unit
    ) : NavigateUpAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    @StringRes titleRes: Int,
    navigateUpAction: NavigateUpAction,
    navController: NavController,
    currentBackStackEntry: NavBackStackEntry?
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 22.sp
            )
        },
        navigationIcon = {
            if (navigateUpAction is NavigateUpAction.Visible) {
                IconButton(
                    onClick = navigateUpAction.onClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.go_back)
                    )
                }
            }
        },
        actions = {
            if (currentBackStackEntry.routeClass() != SettingsRoute::class) {
                IconButton(
                    onClick = { navController.navigate(SettingsRoute) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.settings_screen)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}