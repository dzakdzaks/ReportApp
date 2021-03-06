package com.dzakdzaks.laporanbendahara.ui.main

import androidx.lifecycle.*
import com.dzakdzaks.laporanbendahara.data.MainRepository
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.dzakdzaks.laporanbendahara.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    private val _shouldLoadReports = MutableLiveData<Boolean>()
    val reports: LiveData<Resource<List<Report>>> = _shouldLoadReports.switchMap {
        mainRepository.getReports().asLiveData()
    }

    val reportsData: MutableList<Report> = mutableListOf()


    init {
        _shouldLoadReports.value = true
    }

    fun refresh() {
        Timber.d("onRefreshClicked: On refresh clicked")
        _shouldLoadReports.value = true
    }

    fun onRefreshReportAfterFromDetail() {
        _shouldLoadReports.value = true
    }

    fun customDataHeader(responseListReport: List<Report>): List<Report> {
        val resultList: MutableList<Report> = mutableListOf()
        var prevDate = ""
        reportsData.let {
            if (!it.isNullOrEmpty()) {
                prevDate = it[it.size - 1].getCreatedAtJustDate()
            }
        }
        responseListReport.forEach { item ->
            val currDate = item.getCreatedAtJustDate()
            if (prevDate != currDate) {
                prevDate = currDate
                resultList.add(Report(itemType = Report.ITEM_HEADER, createdAt = item.createdAt))
            }
            resultList.add(item)
        }
        return resultList
    }

}