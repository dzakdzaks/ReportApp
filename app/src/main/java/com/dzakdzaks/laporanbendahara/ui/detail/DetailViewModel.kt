package com.dzakdzaks.laporanbendahara.ui.detail

import android.widget.CheckBox
import androidx.lifecycle.*
import com.dzakdzaks.laporanbendahara.data.MainRepository
import com.dzakdzaks.laporanbendahara.data.remote.model.*
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.dzakdzaks.laporanbendahara.utils.extension.getCreatedAtDate
import com.dzakdzaks.laporanbendahara.utils.extension.parseFromReadableToSimpleFormat
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var report: Report? = null

    private val _shouldAddReport = MutableLiveData<Boolean>()
    val addReportResponse: LiveData<Resource<Created>> = _shouldAddReport.switchMap {
        mainRepository.addReport(addReportData()).asLiveData()
    }

    private val _shouldUpdateReport = MutableLiveData<Boolean>()
    val updateReportResponse: LiveData<Resource<Updated>> = _shouldUpdateReport.switchMap {
        mainRepository.updateReport(updateReportData()).asLiveData()
    }

    private val _shouldDeleteReport = MutableLiveData<Boolean>()
    val deleteReportResponse: LiveData<Resource<Deleted>> = _shouldDeleteReport.switchMap {
        mainRepository.deleteReport(report!!.id).asLiveData()
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
    val checkboxes = mutableListOf<MaterialCheckBox>()
    val checkBoxesValue = mutableListOf<String>()

    var selectedTimeInMillis = System.currentTimeMillis()

    var isAfterUpdate: Boolean = false

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
        val checkBoxString =
            if (other.value.isNullOrEmpty())
                checkBoxesValue.filter { it != "Other" }.joinToString()
            else
                "${checkBoxesValue.filter { it != "Other" }.joinToString()}, ${other.value!!}"
        val list = listOf(
            Report(
                id = (countData + 1).toString(),
                createdAt = Date(System.currentTimeMillis()).getCreatedAtDate(),
                type = typeReport,
                dateIncome = date.value!!.parseFromReadableToSimpleFormat(),
                sourceIncome = title.value!!,
                totalIncome = total.value!!,
                descriptionIncome = desc.value!!,
                recipientAndWitnessIncome = checkBoxString
            )
        )
        Data(list)
    } else {
        val checkBoxString =
            if (other.value.isNullOrEmpty())
                checkBoxesValue.filter { it != "Other" }.joinToString()
            else
                "${checkBoxesValue.filter { it != "Other" }.joinToString()}, ${other.value!!}"
        val list = listOf(
            Report(
                id = (countData + 1).toString(),
                createdAt = Date(System.currentTimeMillis()).getCreatedAtDate(),
                type = typeReport,
                dateExpense = date.value!!.parseFromReadableToSimpleFormat(),
                typeExpense = title.value!!,
                totalExpense = total.value!!,
                whoExpense = checkBoxString,
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


    private fun updateReportData(): Data = if (report!!.type == Report.INCOME) {
        val checkBoxString =
            if (other.value.isNullOrEmpty())
                checkBoxesValue.filter { it != "Other" }.joinToString()
            else
                "${checkBoxesValue.filter { it != "Other" }.joinToString()}, ${other.value!!}"
        val list = listOf(
            Report(
                id = report!!.id,
                createdAt = report!!.createdAt,
                type = report!!.type,
                dateIncome = date.value!!.parseFromReadableToSimpleFormat(),
                sourceIncome = title.value!!,
                totalIncome = total.value!!,
                descriptionIncome = desc.value!!,
                recipientAndWitnessIncome = checkBoxString
            )
        )
        Data(list)
    } else {
        val checkBoxString =
            if (other.value.isNullOrEmpty())
                checkBoxesValue.filter { it != "Other" }.joinToString()
            else
                "${checkBoxesValue.filter { it != "Other" }.joinToString()}, ${other.value!!}"
        val list = listOf(
            Report(
                id = report!!.id,
                createdAt = report!!.createdAt,
                type = report!!.type,
                dateExpense = date.value!!.parseFromReadableToSimpleFormat(),
                typeExpense = title.value!!,
                totalExpense = total.value!!,
                whoExpense = checkBoxString,
                whoReceived = receiver.value!!,
                witnessExpense = witness.value!!,
                descriptionExpense = desc.value!!
            )
        )
        Data(list)
    }

    fun updateReport() {
        _shouldUpdateReport.value = true
        Timber.d("wakwaw ${Gson().toJson(updateReportData())}")
    }

    fun deleteReport() {
        _shouldDeleteReport.value = true
    }

    /** ==========================FINISH API========================= */

}