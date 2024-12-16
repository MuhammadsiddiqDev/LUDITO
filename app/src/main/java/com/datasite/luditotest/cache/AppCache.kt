package com.datasite.luditotest.cache

import android.content.Context
import android.content.SharedPreferences
import com.datasite.luditotest.data.CacheModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppCache private constructor(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("Joymee App", Context.MODE_PRIVATE)

    private val gson = Gson()

    private val CACHE_MODEL_KEY = "cache_model"

    companion object {
        var appCache: AppCache? = null
            private set

        fun init(context: Context) {
            if (appCache == null) {
                appCache = AppCache(context)
            }
        }
    }

    fun saveCacheModel(cacheModel: CacheModel) {
        val currentList = getCacheModelList().toMutableList()

        currentList.add(cacheModel)
        val editor = preferences.edit()
        val json = gson.toJson(currentList)
        editor.putString(CACHE_MODEL_KEY, json)
        editor.apply()
    }

    fun getCacheModelList(): List<CacheModel> {
        val json = preferences.getString(CACHE_MODEL_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<CacheModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun clearCacheModelList() {
        val editor = preferences.edit()
        editor.remove(CACHE_MODEL_KEY)
        editor.apply()
    }
}