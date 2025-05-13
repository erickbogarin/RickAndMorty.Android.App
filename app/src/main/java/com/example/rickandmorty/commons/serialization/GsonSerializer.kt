package com.example.rickandmorty.commons.serialization

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject

class GsonSerializer @Inject constructor(
    private val gson: Gson,
) : JsonSerializer {

    override fun <T> toJson(data: T): String {
        return gson.toJson(data)
    }

    override fun <T> fromJson(json: String, type: Type): T? {
        return gson.fromJson(json, type)
    }

    override fun <T> getType(): Type {
        return object : TypeToken<T>() {}.type
    }

    override fun <T> listType(elementClass: Class<T>): Type {
        return TypeToken.getParameterized(List::class.java, elementClass).type
    }
}
