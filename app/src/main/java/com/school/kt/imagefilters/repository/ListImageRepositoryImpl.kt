package com.school.kt.imagefilters.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ListImageRepositoryImpl : ListImageRepository {

    override fun searchImages(query: String): Result {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.splashbase.co/api/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<ImageService>(ImageService::class.java)
        return try {
            val executor = service.searchImages(query).execute()
            if (executor.isSuccessful) {
                Result.Success(executor.body()?.images)
            } else {
                Result.Fail(executor.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }

    override fun latestImages(): Result {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.splashbase.co/api/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<ImageService>(ImageService::class.java)
        return try {
            val executor = service.latestImages().execute()
            if (executor.isSuccessful) {
                Result.Success(executor.body()?.images)
            } else {
                Result.Fail(executor.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }
}
