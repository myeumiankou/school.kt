package com.school.kt.imagefilters.repository


class ListImageRepositoryImpl(private val imageService: ImageService) : ListImageRepository {

    override fun searchImages(query: String): Result {
        return try {
            with(imageService.searchImages(query).execute()) {
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

    override fun latestImages(): Result {
        return try {
            with(imageService.latestImages().execute()) {
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
}
