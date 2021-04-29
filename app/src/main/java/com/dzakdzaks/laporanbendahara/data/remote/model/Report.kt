package com.dzakdzaks.laporanbendahara.data.remote.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Report(

    @Json(name = "Timestamp")
    val createdAt: String?,
    @Json(name = "Jenis")
    val type: String,

    /*INCOME*/
    @Json(name = "Tanggal Pemasukan")
    val dateIncome: String,
    @Json(name = "Sumber Pemasukan")
    val sourceIncome: String,
    @Json(name = "Jumlah Pemasukan")
    val totalIncome: String,
    @Json(name = "Penerima & Saksi")
    val recipientAndWitnessIncome: String,
    @Json(name = "Keterangan Pemasukan")
    val descriptionIncome: String,
    /*INCOME*/

    /*EXPENSE*/
    @Json(name = "Tanggal Pengeluaran")
    val dateExpense: String,
    @Json(name = "Jenis Pengeluaran")
    val typeExpense: String,
    @Json(name = "Jumlah Pengeluaran")
    val totalExpense: String,
    @Json(name = "Yang Mengeluarkan")
    val whoExpense: String,
    @Json(name = "Yang Menerima")
    val whoReceived: String,
    @Json(name = "Saksi")
    val WitnessExpense: String,
    @Json(name = "Keterangan Pengeluaran")
    val descriptionExpense: String,
    /*EXPENSE*/

    override val itemType: Int = ITEM_DATA
): MultiItemEntity {

    companion object {
        const val ITEM_HEADER = 1
        const val ITEM_DATA = 2
    }
}