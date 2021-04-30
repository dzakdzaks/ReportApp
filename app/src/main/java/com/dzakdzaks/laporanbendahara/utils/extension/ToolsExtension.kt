package com.dzakdzaks.laporanbendahara.utils.extension

import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/** ==========================CURRENCY================================ */

fun String.currencyFormat(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    return format.format(this.toLong())
}
/** ==========================END CURRENCY================================ */


/** ==========================DATE================================ */

fun Date?.getCreatedAtDate(): String {
    var result = ""
    try {
        this?.let {
            result = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("id", "ID")).format(it)
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return result
}

fun String?.parseFromReadableToSimpleFormat(): String {
    var result = "-"
    try {
        this?.let { value ->
            val date = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).parse(value)
            date?.let {
                result = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(it)
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return result
}

fun Date?.convertDateToReadable(): String {
    var result = ""
    try {
        this?.let {
            result = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(it)
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return result
}

fun String?.getFullDateTimeJustDate(): String {
    var result = "-"
    try {
        this?.let { value ->
            val date = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).parse(value)
            date?.let {
                result = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(it)
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return result
}

fun String?.getFullDateTimeJustDateReadable(): String {
    var result = "-"
    try {
        this?.let { value ->
            val date = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).parse(value)
            date?.let {
                result = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(it)
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return result
}

/** ==========================END DATE================================ */
