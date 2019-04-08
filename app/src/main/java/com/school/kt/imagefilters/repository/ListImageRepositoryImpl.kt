package com.school.kt.imagefilters.repository

import com.school.kt.imagefilters.data.RequestResult
import retrofit2.Call


class ListImageRepositoryImpl(private val imageService: ImageService) : ListImageRepository {

    override fun searchImages(query: String): Result = fetchImages(imageService.searchImages(query))

    override fun latestImages(): Result = fetchImages(imageService.latestImages())

    private fun fetchImages(call: Call<RequestResult>): Result = try {
        with(call.execute()) {
            if (isSuccessful) {
                Result.Success(body()?.images)
            } else {
                Result.Fail(code())
            }
        }
    } catch (e: Exception) {
        Result.Error(e.message)
    }
}
