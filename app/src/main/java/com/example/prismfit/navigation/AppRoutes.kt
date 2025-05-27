package com.example.prismfit.navigation

import kotlinx.serialization.Serializable

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
