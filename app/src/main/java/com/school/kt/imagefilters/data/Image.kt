package com.school.kt.imagefilters.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Image {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("large_url")
    @Expose
    var largeUrl: String? = null
    @SerializedName("copyright")
    @Expose
    var copyright: String? = null
    @SerializedName("site")
    @Expose
    var site: String? = null

    override fun toString() : String {
        return "url - $url - large url $largeUrl"
    }
}
