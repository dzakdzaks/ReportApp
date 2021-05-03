package com.dzakdzaks.laporanbendahara.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Count(
        @Json(name = "rows")
        val rows: Int = 0
)