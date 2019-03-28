package com.school.kt.imagefilters.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Image(
    @Expose
    val url: String,
    @SerializedName("large_url")
    @Expose
    val largeUrl: String
) {
    override fun toString() = "url - $url - large url $largeUrl"
}
