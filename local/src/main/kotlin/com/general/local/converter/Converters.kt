package com.general.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun listStringToJson(value: List<String>): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToListString(value: String): List<String> =
        Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun mapAnyToJson(value: Map<String, Any>): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToMapAny(value: String): Map<String, Any> {
//        gson.fromJson(json, new TypeToken<List<Video>>(){}.getType());
//        Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return Gson().fromJson(value, mapType)
    }
}