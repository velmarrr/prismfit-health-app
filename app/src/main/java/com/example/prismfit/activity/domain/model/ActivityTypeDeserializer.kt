package com.example.prismfit.activity.domain.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ActivityTypeDeserializer : JsonDeserializer<ActivityType> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ActivityType {
        return json?.asString?.let { ActivityType.fromString(it) } ?: ActivityType.WALKING
    }
}