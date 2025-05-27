package com.example.prismfit.navigation.utils

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import com.example.prismfit.common.core.ext.replaceLastDotByDollar
import kotlin.reflect.KClass

fun NavBackStackEntry?.routeClass(): KClass<*>? {
    return this?.destination.routeClass()
}

fun NavDestination?.routeClass(): KClass<*>? {
    return this?.route
        ?.split("/")
        ?.first()
        ?.let { className ->
            generateSequence(className) { it.replaceLastDotByDollar() }
                .mapNotNull(::tryParseClass)
                .firstOrNull()
        }
}

private fun tryParseClass(className: String): KClass<*>? {
    return runCatching { Class.forName(className).kotlin }.getOrNull()
}
