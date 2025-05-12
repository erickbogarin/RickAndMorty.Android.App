package com.example.rickandmorty.commons.storage

interface LocalStorage {
    fun getString(key: String, defaultValue: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
    fun contains(key: String): Boolean
    fun clear()
}