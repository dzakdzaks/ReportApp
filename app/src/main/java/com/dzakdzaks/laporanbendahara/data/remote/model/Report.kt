package com.dzakdzaks.laporanbendahara.data.remote.model

import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.dzakdzaks.laporanbendahara.utils.extension.currencyFormat
import com.dzakdzaks.laporanbendahara.utils.extension.getFullDateTimeJustDate
import com.dzakdzaks.laporanbendahara.utils.extension.getFullDateTimeJustDateReadable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@JsonClass(generateAdapter = true)
@Parcelize
data class Report(

        @Json(name = "ID")
        val id: String = UUID.randomUUID().toString(),
        @Json(name = "Timestamp")
        val createdAt: String?,
        @Json(name = "Jenis")
        val type: String = "",

        /*INCOME*/
        @Json(name = "Tanggal Pemasukan")
        val dateIncome: String = "",
        @Json(name = "Sumber Pemasukan")
        val sourceIncome: String = "",
        @Json(name = "Jumlah Pemasukan")
        val totalIncome: String = "",
        @Json(name = "Penerima & Saksi")
        val recipientAndWitnessIncome: String = "",
        @Json(name = "Keterangan Pemasukan")
        val descriptionIncome: String = "",
        /*INCOME*/

        /*EXPENSE*/
        @Json(name = "Tanggal Pengeluaran")
        val dateExpense: String = "",
        @Json(name = "Jenis Pengeluaran")
        val typeExpense: String = "",
        @Json(name = "Jumlah Pengeluaran")
        val totalExpense: String = "",
        @Json(name = "Yang Mengeluarkan")
        val whoExpense: String = "",
        @Json(name = "Yang Menerima")
        val whoReceived: String = "",
        @Json(name = "Saksi")
        val witnessExpense: String = "",
        @Json(name = "Keterangan Pengeluaran")
        val descriptionExpense: String = "",
        /*EXPENSE*/

        @Transient
        override val itemType: Int = ITEM_DATA
) : Parcelable, MultiItemEntity {

    fun getTotalIncomeCurrencyFormat(): String = totalIncome.currencyFormat()

    fun getTotalExpenseCurrencyFormat(): String = totalExpense.currencyFormat()

    fun getCreatedAtJustDate(): String = createdAt.getFullDateTimeJustDate()

    fun getCreatedAtJustDateReadable(): String = createdAt.getFullDateTimeJustDateReadable()

    companion object {
        const val ITEM_HEADER = 1
        const val ITEM_DATA = 2

        const val INCOME = "Pemasukan"
        const val EXPENSE = "Pengeluaran"
    }
}