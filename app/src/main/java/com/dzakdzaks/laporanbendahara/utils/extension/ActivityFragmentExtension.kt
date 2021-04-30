package com.dzakdzaks.laporanbendahara.utils.extension

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

fun Context.startIntent(activityClass: Class<*>, data: (Intent) -> Unit = {}) {
    val intent = Intent(this, activityClass)
    data.invoke(intent)
    startActivity(intent)
}

fun Context.startIntentResult(activityClass: Class<*>, data: (Intent) -> Unit = {}): Intent {
    val intent = Intent(this, activityClass)
    data.invoke(intent)
    return intent
}

fun FragmentActivity.hideSoftKeyBoard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isAcceptingText) {
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}

fun FragmentActivity.showSoftKeyboard(editText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    editText.postDelayed({
        editText.requestFocus()
        imm.showSoftInput(editText, 0)
    }, 100)
}

fun FragmentActivity.showMaterialDatePicker(isCanSelectYesterdayDate: Boolean = true, currentSelectedTimeInMillis: Long, bind: (Long) -> Unit) {
    val builder = MaterialDatePicker.Builder.datePicker()
    builder.setTitleText("Pilih Tanggal")
    if (!isCanSelectYesterdayDate)
        builder.setCalendarConstraints(CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build())
    builder.setSelection(currentSelectedTimeInMillis)
    val materialDatePicker = builder.build()
    materialDatePicker.addOnPositiveButtonClickListener {
        bind.invoke(it)
    }

    materialDatePicker.show(supportFragmentManager, materialDatePicker.toString())
}