package com.school.kt.imagefilters.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestResult {

    @SerializedName("images")
    @Expose
    var images: List<Image>? = null

}
