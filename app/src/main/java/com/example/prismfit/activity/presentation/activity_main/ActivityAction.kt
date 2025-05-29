package com.example.prismfit.activity.presentation.activity_main

import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.domain.model.ActivityType

sealed interface ActivityAction {
    data class OnStart(val type: ActivityType) : ActivityAction
    data class OnActivityClick(val activity: Activity) : ActivityAction
}