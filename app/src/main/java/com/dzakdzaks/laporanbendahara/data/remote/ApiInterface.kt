package com.dzakdzaks.laporanbendahara.data.remote

import com.dzakdzaks.laporanbendahara.data.remote.model.*
import com.dzakdzaks.laporanbendahara.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface ApiInterface {

    companion object {
        const val BASE_URL = "https://sheetdb.io/api/v1/"
        const val SHEET_DB = "4rxz3ate6ezks"
    }

    @GET(SHEET_DB)
    fun getReports(
        @Query("sort_by") sortBy: String = "ID",
        @Query("sort_order") sortOrder: String = "desc"
    ): Flow<Resource<List<Report>>>

    @POST(SHEET_DB)
    fun addReport(
          @Body data: Data
    ): Flow<Resource<Created>>

    @PUT("$SHEET_DB/{column}/{value}")
    fun updateReport(
        @Path("column") column: String = "ID",
        @Path("value") value: String,
        @Body data: Data
    ): Flow<Resource<Updated>>

    @DELETE("$SHEET_DB/{column}/{value}")
    fun deleteReport(
        @Path("column") column: String = "ID",
        @Path("value") value: String
    ): Flow<Resource<Deleted>>
}