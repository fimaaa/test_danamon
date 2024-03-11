package com.general.common.extension

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

val gson = Gson()
fun CustomGson(): Gson {
    return GsonBuilder()
        .serializeSpecialFloatingPointValues() // Serialize special floating-point values
        .create()
}

fun JsonElement.getInt(): Int = if (isJsonPrimitive && asJsonPrimitive.isNumber) asInt else 0
fun JsonElement.getString(): String =
    if (isJsonPrimitive && asJsonPrimitive.isString) (if (isJsonArray) getFirstArrayString() else asString) else ""

fun JsonElement.getDouble(): Double =
    if (isJsonPrimitive && asJsonPrimitive.isNumber) asDouble else 0.0

fun JsonElement.getLong(): Long = if (isJsonPrimitive && asJsonPrimitive.isNumber) asLong else 0
fun JsonElement.getBoolean(): Boolean =
    if (isJsonPrimitive && asJsonPrimitive.isBoolean) asBoolean else false

fun JsonElement.getJsonArray(): JsonArray = if (isJsonArray) asJsonArray else JsonArray()

fun JsonElement.getFirstArrayString() = asJsonArray.firstOrNull()?.asString ?: ""

fun JsonObject.getInt(attribute: String): Int =
    if (this.has(attribute) && this[attribute].isJsonPrimitive && this[attribute].asJsonPrimitive.isNumber) this[attribute].asInt else 0

fun JsonObject.getString(attribute: String): String =
    if (this.has(attribute) && this[attribute].isJsonPrimitive && this[attribute].asJsonPrimitive.isString) (if (this[attribute].isJsonArray) this.getFirstArrayString(
        attribute
    ) else this[attribute].asString) else ""

fun JsonObject.getDouble(attribute: String): Double =
    if (this.has(attribute) && this[attribute].isJsonPrimitive && this[attribute].asJsonPrimitive.isNumber) this[attribute].asDouble else 0.0

fun JsonObject.getLong(attribute: String): Long =
    if (this.has(attribute) && this[attribute].isJsonPrimitive && this[attribute].asJsonPrimitive.isNumber) this[attribute].asLong else 0

fun JsonObject.getBoolean(attribute: String): Boolean =
    if (this.has(attribute) && this[attribute].isJsonPrimitive && this[attribute].asJsonPrimitive.isBoolean) this[attribute].asBoolean else false

fun JsonObject.getJsonArray(attribute: String): JsonArray =
    if (this.has(attribute) && this[attribute].isJsonArray) this[attribute].asJsonArray else JsonArray()

fun JsonObject.isNotNull(attribute: String) = this[attribute] != null

fun JsonObject.getFirstArrayString(attribute: String) =
    this[attribute].asJsonArray.firstOrNull()?.asString ?: ""

fun JSONObject.toUrlParameter(bundle: Bundle = Bundle()): Bundle? {
    val it = this.keys()
    while (it.hasNext()) {
        val key = it.next()
        val arr = this.optJSONArray(key)
        val num = this.optDouble(key)
        val str = this.optString(key)
        var json: JSONObject? = null
        try {
            json = JSONObject(str)
        } catch (e: Exception) {
        }
        if (arr != null && arr.length() <= 0) bundle.putStringArray(
            key,
            arrayOf()
        ) else if (arr != null && !java.lang.Double.isNaN(arr.optDouble(0))) {
            val newArr = DoubleArray(arr.length())
            for (i in 0 until arr.length()) newArr[i] = arr.optDouble(i)
            bundle.putDoubleArray(key, newArr)
        } else if (arr?.optString(0) != null) {
            val newArr = arrayOfNulls<String>(arr.length())
            for (i in 0 until arr.length()) newArr[i] = arr.optString(i)
            bundle.putStringArray(key, newArr)
        } else if (!num.isNaN())
            bundle.putDouble(key, num)
        else if (json != null) {
            bundle.putAll(json.toUrlParameter())
        } else if (str != null)
            bundle.putString(key, str)
        else System.err.println("unable to transform json to bundle $key")
    }
    return bundle
}

fun Map<String, Any>.getJsonFromMap(): JSONObject {
    val jsonData = JSONObject()
    for (key in this.keys) {
        var value = this[key]
        if (value is Map<*, *>) {
            value = (value as Map<String, Any>).getJsonFromMap()
        }
        try {
            jsonData.put(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return jsonData
}

fun Bundle.getJsonFromBundle(): JSONObject {
    val jsonData = JSONObject()
    val keys: Set<String> = this.keySet()
    for (key in keys) {
        try {
            // json.put(key, bundle.get(key)); see edit below
            jsonData.put(key, JSONObject.wrap(this.get(key)))
        } catch (e: JSONException) {
            // Handle exception here
            e.printStackTrace()
        }
    }
    return jsonData
}

// convert a data class to a map
fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

// convert a map to a data class
inline fun <reified T> Map<String, Any>.toDataClass(): T {
    return convert()
}

// convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}