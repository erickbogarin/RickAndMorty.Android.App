package com.example.rickandmorty.commons.storage

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

class SharedPreferencesStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalStorage {
    
    override fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
    
    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    
    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
    
    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
    
    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}