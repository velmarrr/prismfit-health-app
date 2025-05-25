package com.example.prismfit.activity.domain.model

enum class ActivityType(val typeName: String) {
    WALKING("walking"),
    RUNNING("running"),
    CYCLING("cycling");

    companion object {
        fun fromString(type: String): ActivityType {
            return entries.find { it.typeName == type } ?: WALKING
        }
    }
}