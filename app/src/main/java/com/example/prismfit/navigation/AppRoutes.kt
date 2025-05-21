package com.example.prismfit.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data object HomeGraph {
    @Serializable
    data object HomeRoute
}

@Serializable
data object ActivityGraph {
    @Serializable
    data object ActivityMainRoute

    @Serializable
    data class PendingActivityRoute(val selectedType: String)

    @Serializable
    data class ActivityMapRoute(val routeJson: String)
}

@Serializable
data object DietGraph {
    @Serializable
    data object DietRoute

    @Serializable
    data object AddDietRoute

    @Serializable
    data class EditDietRoute(val mealId: String)
}

@Serializable
data object NotesGraph {
    @Serializable
    data object NotesRoute

    @Serializable
    data object AddNoteRoute

    @Serializable
    data class EditNoteRoute(val noteId: String)
}

@Serializable
data object SettingsGraph {
    @Serializable
    data object SettingsRoute
}

@Serializable
data object RegisterGraph {
    @Serializable
    data object RegisterRoute
}

@Serializable
data object LoginGraph {
    @Serializable
    data object LoginRoute
}

fun NavBackStackEntry?.routeClass(): KClass<*>? {
    return this?.destination.routeClass()
}

fun NavDestination?.routeClass(): KClass<*>? {
    return this?.route
        ?.split("/")
        ?.first()
        ?.let { className ->
            generateSequence(className, ::replaceLastDotByDollar)
                .mapNotNull(::tryParseClass)
                .firstOrNull()
        }
}

private fun tryParseClass(className: String): KClass<*>? {
    return runCatching { Class.forName(className).kotlin }.getOrNull()
}

private fun replaceLastDotByDollar(input: String): String? {
    val index = input.lastIndexOf('.')
    return if (index != -1) {
        String(input.toCharArray().apply { set(index, '$') })
    } else {
        null
    }
}