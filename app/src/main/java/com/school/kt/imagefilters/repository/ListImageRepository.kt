package com.school.kt.imagefilters.repository

import com.school.kt.imagefilters.data.Image
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ListImageRepository {

    sealed class Result {
        data class Success(val list: List<Image>?) : Result()
        data class Fail(val errorCode: Int) : Result()
        data class Error(val description: String?) : Result()
    }

    fun searchImages(query: String): Result {
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

    fun latestImages(): Result {
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
