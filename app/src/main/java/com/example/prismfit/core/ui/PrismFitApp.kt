package com.example.prismfit.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.prismfit.R
import com.example.prismfit.activity.presentation.ActivityMainScreen
import com.example.prismfit.activity.presentation.PendingActivityScreen
import com.example.prismfit.core.session.LocalSessionManager
import com.example.prismfit.auth.presentation.login.LoginScreen
import com.example.prismfit.auth.presentation.registration.RegistrationScreen
import com.example.prismfit.diet.presentation.add_diet.AddDietScreen
import com.example.prismfit.diet.presentation.diet_main.DietScreen
import com.example.prismfit.home.presentation.HomeScreen
import com.example.prismfit.navigation.ActivityGraph
import com.example.prismfit.navigation.ActivityGraph.ActivityMainRoute
import com.example.prismfit.navigation.ActivityGraph.PendingActivityRoute
import com.example.prismfit.navigation.DietGraph
import com.example.prismfit.navigation.DietGraph.AddDietRoute
import com.example.prismfit.navigation.DietGraph.DietRoute
import com.example.prismfit.navigation.HomeGraph
import com.example.prismfit.navigation.HomeGraph.HomeRoute
import com.example.prismfit.navigation.LocalNavController
import com.example.prismfit.navigation.LoginGraph.LoginRoute
import com.example.prismfit.navigation.MainTabs
import com.example.prismfit.navigation.NotesGraph
import com.example.prismfit.navigation.NotesGraph.AddNoteRoute
import com.example.prismfit.navigation.NotesGraph.NotesRoute
import com.example.prismfit.navigation.ProfileGraph
import com.example.prismfit.navigation.ProfileGraph.ProfileRoute
import com.example.prismfit.navigation.RegisterGraph.RegisterRoute
import com.example.prismfit.navigation.SettingsGraph
import com.example.prismfit.navigation.SettingsGraph.SettingsRoute
import com.example.prismfit.navigation.routeClass
import com.example.prismfit.notes.presentation.add_note.AddNoteScreen
import com.example.prismfit.notes.presentation.notes_list.NotesScreen
import com.example.prismfit.profile.presentation.ProfileScreen
import com.example.prismfit.settings.presentation.SettingsScreen
import com.example.prismfit.core.ui.theme.AppTheme
import com.example.prismfit.navigation.DietGraph.EditDietRoute
import com.example.prismfit.navigation.NotesGraph.EditNoteRoute

@Composable
fun PrismFitApp() {
    val sessionManager = LocalSessionManager.current
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState()
    val navController = rememberNavController()

    if (isLoggedIn) {
        PrismFitAppContent(navController = navController)
    } else {
        NavHost(
            navController = navController,
            startDestination = LoginRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<LoginRoute> { LoginScreen(navController) }
            composable<RegisterRoute> { RegistrationScreen(navController) }
        }
    }
}

@Composable
fun PrismFitAppContent(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val titleRes = when (currentBackStackEntry.routeClass()) {
        HomeRoute::class -> R.string.home_screen
        ActivityMainRoute::class -> R.string.activity_main_screen
        PendingActivityRoute::class -> R.string.pending_activity_screen
        DietRoute::class -> R.string.diet_screen
        AddDietRoute::class -> R.string.add_diet_screen
        NotesRoute::class -> R.string.notes_screen
        AddNoteRoute::class -> R.string.add_note_screen
        ProfileRoute::class -> R.string.profile_screen
        SettingsRoute::class -> R.string.settings_screen
        else -> R.string.app_name
    }
    Scaffold(
        topBar = {
            if (currentBackStackEntry.routeClass() != LoginRoute::class &&
                currentBackStackEntry.routeClass() != RegisterRoute::class
            ) {
                AppToolbar(
                    navigateUpAction = if (navController.previousBackStackEntry == null) {
                        NavigateUpAction.Hidden
                    } else {
                        NavigateUpAction.Visible(
                            onClick = { navController.navigateUp() }
                        )
                    },
                    titleRes = titleRes,
                    navController = navController,
                    currentBackStackEntry = currentBackStackEntry
                )
            }
        },
        floatingActionButton = {
            if (currentBackStackEntry.routeClass() == DietRoute::class) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddDietRoute) },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_diet_screen),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_diet_screen)
                        )
                    }
                }
            } else if (currentBackStackEntry.routeClass() == NotesRoute::class) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddNoteRoute) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_note_screen)
                    )
                }
            }
        },
        bottomBar = {
            if (currentBackStackEntry.routeClass() != ProfileRoute::class &&
                currentBackStackEntry.routeClass() != SettingsRoute::class &&
                currentBackStackEntry.routeClass() != LoginRoute::class &&
                currentBackStackEntry.routeClass() != RegisterRoute::class
            ) {
                AppNavigationBar(navController = navController, tabs = MainTabs)
            }
        }
    ) { paddingValues ->
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeGraph,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                navigation<HomeGraph>(startDestination = HomeRoute) {
                    composable<HomeRoute> { HomeScreen() }
                }
                navigation<ActivityGraph>(startDestination = ActivityMainRoute) {
                    composable<ActivityMainRoute> { ActivityMainScreen() }
                    composable<PendingActivityRoute> { PendingActivityScreen() }
                }
                navigation<DietGraph>(startDestination = DietRoute) {
                    composable<DietRoute> { DietScreen(
                        onMealClick = { mealId ->
                            navController.navigate(EditDietRoute(mealId))
                        }
                    ) }
                    composable<AddDietRoute> { AddDietScreen(mealId = null) }
                    composable<EditDietRoute> { AddDietScreen(mealId = it.arguments?.getString("mealId")) }
                }
                navigation<NotesGraph>(startDestination = NotesRoute) {
                    composable<NotesRoute> { NotesScreen(
                        onNoteClick = { noteId ->
                            navController.navigate(EditNoteRoute(noteId))
                        }
                    ) }
                    composable<AddNoteRoute> { AddNoteScreen(noteId = null) }
                    composable<EditNoteRoute> { AddNoteScreen(noteId = it.arguments?.getString("noteId")) }
                }
                navigation<ProfileGraph>(startDestination = ProfileRoute) {
                    composable<ProfileRoute> { ProfileScreen(navController) }
                }
                navigation<SettingsGraph>(startDestination = SettingsRoute) {
                    composable<SettingsRoute> { SettingsScreen() }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppPreview() {
    AppTheme {
        PrismFitAppContent(rememberNavController())
    }
}