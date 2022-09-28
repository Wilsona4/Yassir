package com.example.yassir.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.yassir.data.model.details.*
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ProvidedTypeConverter
class Converter @Inject constructor(
    private val moshi: Moshi
) {
    @TypeConverter
    fun saveIntList(list: List<Int>?): String? {
        val listType = object : TypeToken<List<Int?>?>() {}.type
        val adapter: JsonAdapter<List<Int>>? = moshi.adapter(listType)
        return adapter?.toJson(list)
    }

    @TypeConverter
    fun getIntList(json: String): List<Int>? {
        val listType = object : TypeToken<List<Int?>?>() {}.type
        val adapter: JsonAdapter<List<Int>>? = moshi.adapter(listType)
        return if (json.isEmpty()) emptyList() else adapter?.fromJson(json)
    }


    /*TODO Kindly Ignore unused converters*/

    //DomainGenre Entity Converter
    @TypeConverter
    fun fromListOfGenre(value: List<Genre>?): String? {
        val listType = object : TypeToken<List<Genre?>?>() {}.type
        val adapter: JsonAdapter<List<Genre>>? = moshi.adapter(listType)
        return adapter?.toJson(value)
    }

    @TypeConverter
    fun toListOfGenre(json: String?): List<Genre>? {
        val listType = object : TypeToken<List<Genre?>?>() {}.type
        val adapter: JsonAdapter<List<Genre>>? = moshi.adapter(listType)
        return if (json.isNullOrEmpty()) emptyList() else adapter?.fromJson(json)
    }

    //DomainGenre Entity Converter
    @TypeConverter
    fun fromListOfCollection(value: BelongsToCollection?): String? {
        val listType = object : TypeToken<BelongsToCollection?>() {}.type
        val adapter: JsonAdapter<BelongsToCollection>? = moshi.adapter(listType)
        return adapter?.toJson(value)
    }

    @TypeConverter
    fun toListOfCollection(json: String?): BelongsToCollection? {
        val listType = object : TypeToken<BelongsToCollection?>() {}.type
        val adapter: JsonAdapter<BelongsToCollection>? = moshi.adapter(listType)
        return if (json.isNullOrEmpty()) null else adapter?.fromJson(json)
    }

    //Production Company Entity Converter
    @TypeConverter
    fun fromListOfProductionCompany(value: List<ProductionCompany>?): String? {
        val listType = object : TypeToken<List<ProductionCompany?>?>() {}.type
        val adapter: JsonAdapter<List<ProductionCompany>>? = moshi.adapter(listType)
        return adapter?.toJson(value)
    }

    @TypeConverter
    fun toListOfProductionCompany(json: String?): List<ProductionCompany>? {
        val listType = object : TypeToken<List<ProductionCompany?>?>() {}.type
        val adapter: JsonAdapter<List<ProductionCompany>>? = moshi.adapter(listType)
        return if (json.isNullOrEmpty()) emptyList() else adapter?.fromJson(json)
    }

    //Production Company Entity Converter
    @TypeConverter
    fun fromListOfProductionCountry(value: List<ProductionCountry>?): String? {
        val listType = object : TypeToken<List<ProductionCountry?>?>() {}.type
        val adapter: JsonAdapter<List<ProductionCountry>>? = moshi.adapter(listType)
        return adapter?.toJson(value)
    }

    @TypeConverter
    fun toListOfProductionCountry(json: String?): List<ProductionCountry>? {
        val listType = object : TypeToken<List<ProductionCountry?>?>() {}.type
        val adapter: JsonAdapter<List<ProductionCountry>>? = moshi.adapter(listType)
        return if (json.isNullOrEmpty()) emptyList() else adapter?.fromJson(json)
    }

    //Spoken Languages Entity Converter
    @TypeConverter
    fun fromListOfSpokenLanguagesEntity(value: List<SpokenLanguage>?): String? {
        val listType = object : TypeToken<List<SpokenLanguage?>?>() {}.type
        val adapter: JsonAdapter<List<SpokenLanguage>>? = moshi.adapter(listType)
        return adapter?.toJson(value)
    }

    @TypeConverter
    fun toListOfSpokenLanguagesEntity(json: String?): List<SpokenLanguage>? {
        val listType = object : TypeToken<List<SpokenLanguage?>?>() {}.type
        val adapter: JsonAdapter<List<SpokenLanguage>>? = moshi.adapter(listType)
        return if (json.isNullOrEmpty()) emptyList() else adapter?.fromJson(json)
    }

//    Using Kotlinx
//    @TypeConverter
//    fun saveIntList(list: List<Int>): String {
//        return Json.encodeToString(list)
//    }
//
//    @TypeConverter
//    fun getIntList(stringValue: String): List<Int> {
//        return Json.decodeFromString(stringValue)
//            .mapNotNull {
//                it.toIntOrNull()
//            }
//    }


//    Using Gson
//    @TypeConverter
//    fun saveIntList(list: List<Int>?): String? {
//        return Gson().toJson(list)
//    }
//
//    @TypeConverter
//    fun getIntList(list: String): List<Int>? {
//        return Gson().fromJson(
//            list,
//            object : TypeToken<List<Int>>() {}.type
//        )
//    }
}