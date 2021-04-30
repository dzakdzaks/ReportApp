package com.dzakdzaks.laporanbendahara.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dzakdzaks.laporanbendahara.data.remote.ApiInterface
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.utils.extension.convertDateToReadable
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
        private val apiInterface: ApiInterface
): ViewModel() {

    val report: Report? = null

    val date = MutableLiveData(Date(System.currentTimeMillis()).convertDateToReadable())
    val title = MutableLiveData("")
    val total = MutableLiveData("")
    val desc = MutableLiveData("")


    fun getSourceIncome(): List<String> = listOf(
            "Teromol Masjid Harian",
            "Infaq Perorangan",
            "Infaq Non Perorangan",
            "Voucher Multi Amal Jariyah Ramadhan",
            "Voucher Santunan Yatim & Janda Dhuafa",
            "Voucher",
            "Lain-lain",
    )

    fun getTypeExpense(): List<String> = listOf(
            "Transport Ustad / Narasumber",
            "Belanja Konsumsi",
            "Belanja Cetak (Fotokopi, Spanduk, dll)",
            "Belanja ATK",
            "Kegiatan",
            "Lainnya",
    )

    fun getListReceiverAndWitness(): List<String> = listOf()

}