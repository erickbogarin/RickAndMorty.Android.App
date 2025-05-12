package com.example.rickandmorty.commons.serialization

import java.lang.reflect.Type

interface JsonSerializer {
    fun <T> toJson(data: T): String
    fun <T> fromJson(json: String, type: Type): T?
    fun <T> getType(): Type
    
    // Adicionando um método específico para listas
    fun <T> listType(elementClass: Class<T>): Type
}