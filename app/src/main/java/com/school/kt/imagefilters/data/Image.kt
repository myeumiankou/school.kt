package com.school.kt.imagefilters.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Image(
    @Expose
    val url: String,
    @SerializedName("large_url")
    @Expose
    val largeUrl: String
) : Parcelable {
    override fun toString() = "url - $url - large url $largeUrl"
}
