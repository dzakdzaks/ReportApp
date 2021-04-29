package com.dzakdzaks.laporanbendahara.data

import com.dzakdzaks.laporanbendahara.data.remote.ApiInterface
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    fun getReports() = apiInterface.getReports()

}