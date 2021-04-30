package com.dzakdzaks.laporanbendahara.ui.detail

import androidx.lifecycle.ViewModel
import com.dzakdzaks.laporanbendahara.data.remote.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
        private val apiInterface: ApiInterface
): ViewModel() {




    fun getSourceIncome(): List<String> = listOf(
            "Teromol Masjid Harian",
            "Infaq Perorangan",
            "Infaq Non Perorangan",
            "Voucher Multi Amal Jariyah Ramadhan",
            "Voucher Santunan Yatim & Janda Dhuafa",
            "Voucher",
            "Lain-lain",
    )

    fun getListReceiverAndWitness(): List<String> = listOf()

}