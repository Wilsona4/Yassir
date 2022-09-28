package com.example.yassir.util

import android.util.Log
import com.example.yassir.data.model.movie.MovieErrorResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException

fun HttpException.handleHttpException(): MovieErrorResponse? {
    return try {
        this.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
                .adapter(MovieErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (t: Throwable) {
        Log.d("MovieErrorResponse", "Error while handling httpException $t")
        null
    }
}