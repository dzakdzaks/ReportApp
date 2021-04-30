package com.dzakdzaks.laporanbendahara.ui.detail

import androidx.lifecycle.*
import com.dzakdzaks.laporanbendahara.data.MainRepository
import com.dzakdzaks.laporanbendahara.data.remote.ApiInterface
import com.dzakdzaks.laporanbendahara.data.remote.model.Created
import com.dzakdzaks.laporanbendahara.data.remote.model.Data
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.dzakdzaks.laporanbendahara.utils.extension.convertDateToReadable
import com.dzakdzaks.laporanbendahara.utils.extension.getCreatedAtDate
import com.dzakdzaks.laporanbendahara.utils.extension.parseFromReadableToSimpleFormat
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
        private val mainRepository: MainRepository
): ViewModel() {

    private val _shouldAddReport = MutableLiveData<Boolean>()
    val addReportResponse: LiveData<Resource<Created>> = _shouldAddReport.switchMap {
        mainRepository.addReport(addReportData()).asLiveData()
    }
    var countData: Int = 0

    val date = MutableLiveData("")
    val title = MutableLiveData("")
    val total = MutableLiveData("")
    val desc = MutableLiveData("")
    val other = MutableLiveData("")
    val receiver = MutableLiveData("")
    val witness = MutableLiveData("")

    var typeReport: String = ""
    val checkBoxValues = mutableListOf<String>()

    var selectedTimeInMillis = System.currentTimeMillis()

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

    fun getListReceiverAndWitness(): List<String> = listOf(
            "Faat",
            "Daffa",
            "Gibran",
            "Kathan",
            "Ali",
            "Rek Masjid",
            "Other",
    )

    fun getListWhoExpense(): List<String> = listOf(
            "Faat",
            "Other",
    )

    /** ==========================API========================= */

    private fun addReportData(): Data = if (typeReport == Report.INCOME) {
        val list = listOf(
            Report(
                id = (countData + 1).toString(),
                createdAt = Date(System.currentTimeMillis()).getCreatedAtDate(),
                type = typeReport,
                dateIncome = date.value!!.parseFromReadableToSimpleFormat(),
                sourceIncome = title.value!!,
                totalIncome = total.value!!,
                descriptionIncome = desc.value!!,
                recipientAndWitnessIncome = "${checkBoxValues.filter { it != "Other" }.joinToString()}, ${other.value!!}"
            )
        )
        Data(list)
    } else {
       val list = listOf(
            Report(
                id = (countData + 1).toString(),
                createdAt = Date(System.currentTimeMillis()).getCreatedAtDate(),
                type = typeReport,
                dateExpense = date.value!!.parseFromReadableToSimpleFormat(),
                typeExpense = title.value!!,
                totalExpense = total.value!!,
                whoExpense = "${checkBoxValues.filter { it != "Other" }.joinToString()}, ${other.value!!}",
                whoReceived = receiver.value!!,
                witnessExpense = witness.value!!,
                descriptionExpense = desc.value!!
            )
        )
        Data(list)
    }

    fun addReport() {
        _shouldAddReport.value = true
        Timber.d("wakwaw ${Gson().toJson(addReportData())}")
    }

    /** ==========================FINISH API========================= */

}