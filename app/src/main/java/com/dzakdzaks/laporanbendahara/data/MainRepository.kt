package com.dzakdzaks.laporanbendahara.data

import com.dzakdzaks.laporanbendahara.data.remote.ApiInterface
import com.dzakdzaks.laporanbendahara.data.remote.model.Data
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    fun getReports() = apiInterface.getReports()

    fun addReport(list: Data) = apiInterface.addReport(list)

    fun updateReport(list: Data) = apiInterface.updateReport(value = list.data?.get(0)?.id ?: "0", data = list)

    fun deleteReport(id: String) = apiInterface.deleteReport(value = id)

    fun getCountReport() = apiInterface.getCountReport()
}