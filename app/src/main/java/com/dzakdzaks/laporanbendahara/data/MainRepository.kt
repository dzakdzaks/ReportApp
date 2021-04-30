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
}