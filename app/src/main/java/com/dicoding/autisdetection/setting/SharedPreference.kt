package com.dicoding.autisdetection.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SharedPreference private constructor(private val dataStore: DataStore<Preferences>){

    private val KEY_TOKEN = stringPreferencesKey("token")
    private val KEY_USERNAME = stringPreferencesKey("username")
    private val KEY_EMAIL = stringPreferencesKey("email")
    private val KEY_ID = stringPreferencesKey("id")
    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_STATE = booleanPreferencesKey("state")




    data class User(
        val token: String,
        val username: String,
        val email: String,
        val id: String,
        val name: String
    )

    fun getToken(): Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[KEY_TOKEN] ?: ""
        }
    }


    fun getUser(): Flow<User>{
        return dataStore.data.map { preferences ->
            User(
                preferences[KEY_TOKEN] ?: "",
                preferences[KEY_USERNAME] ?: "",
                preferences[KEY_EMAIL] ?: "",
                preferences[KEY_ID] ?: "",
                preferences[KEY_NAME] ?: ""
            )
        }
    }


    fun isLoggedIn(): Flow<Boolean>{
        return dataStore.data.map { preferences ->
            preferences[KEY_STATE] ?: false
        }
    }

    suspend fun saveToken(token: String){
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    suspend fun isState(state: Boolean){
        dataStore.edit { preferences ->
            preferences[KEY_STATE] = state
        }
    }

    suspend fun logout(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: SharedPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SharedPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SharedPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }










}