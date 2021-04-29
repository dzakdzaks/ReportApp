package com.dzakdzaks.laporanbendahara.data.remote

import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ApiInterface {

    companion object {
        const val BASE_URL = "https://sheetdb.io/api/v1/"
    }

    @GET("4rxz3ate6ezks")
    fun getReports(): Flow<Resource<List<Report>>>
}