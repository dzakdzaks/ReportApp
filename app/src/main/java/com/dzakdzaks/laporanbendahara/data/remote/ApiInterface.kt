package com.dzakdzaks.laporanbendahara.data.remote

import com.dzakdzaks.laporanbendahara.data.remote.model.Created
import com.dzakdzaks.laporanbendahara.data.remote.model.Data
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    companion object {
        const val BASE_URL = "https://sheetdb.io/api/v1/"
        const val SHEET_DB = "4rxz3ate6ezks"
    }

    @GET(SHEET_DB)
    fun getReports(
        /*@Query("limit") limit: Int = 20,*/
        @Query("sort_by") sortBy: String = "ID",
        @Query("sort_order") sortOrder: String = "desc"
    ): Flow<Resource<List<Report>>>

    @POST(SHEET_DB)
    fun addReport(
          @Body data: Data
    ): Flow<Resource<Created>>
}