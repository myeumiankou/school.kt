package com.school.kt.imagefilters.repository

import com.school.kt.imagefilters.data.Image
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ListImageRepository {

    fun searchImages(query: String): List<Image>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.splashbase.co/api/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<ImageService>(ImageService::class.java)
        val result = service.searchImages(query).execute().body()

        return if (result != null) {
            result.images
        } else {
            emptyList()
        }
    }

    fun latestImages(): List<Image>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.splashbase.co/api/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<ImageService>(ImageService::class.java)
        val result = service.latestImages().execute().body()

        return if (result != null) {
            result.images
        } else {
            emptyList()
        }
    }
}
