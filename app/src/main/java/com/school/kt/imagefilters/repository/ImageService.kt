package com.school.kt.imagefilters.repository

import com.school.kt.imagefilters.data.RequestResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @see http://www.splashbase.co/api
 */
interface ImageService {

    @GET("search")
    fun searchImages(@Query("query") query: String): Call<RequestResult>

    @GET("latest?images_only=true")
    fun latestImages(): Call<RequestResult>
}