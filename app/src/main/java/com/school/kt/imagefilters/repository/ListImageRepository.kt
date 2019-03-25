package com.school.kt.imagefilters.repository

import com.school.kt.imagefilters.data.Image

interface ListImageRepository {
    fun searchImages(query: String): Result
    fun latestImages(): Result
}

sealed class Result {
    data class Success(val list: List<Image>?) : Result()
    data class Fail(val errorCode: Int) : Result()
    data class Error(val description: String?) : Result()
}