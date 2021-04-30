package com.dzakdzaks.laporanbendahara.data.remote.model

import com.squareup.moshi.Json

data class Created(
        @Json(name = "created")
        val created: String?
)