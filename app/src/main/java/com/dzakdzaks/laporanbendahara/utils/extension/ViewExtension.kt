package com.dzakdzaks.laporanbendahara.utils.extension

import android.view.View
import androidx.core.view.isVisible

fun visibleMultipleViews(vararg views: View) {
    views.forEach {
        it.visible()
    }
}

fun goneMultipleViews(vararg views: View) {
    views.forEach {
        it.gone()
    }
}

fun invisibleMultipleViews(vararg views: View) {
    views.forEach {
        it.invisible()
    }
}

fun View.clickableOrNot(isLoading: Boolean) {
    this.isClickable = !isLoading
}

fun View.visible() {
    post {
        if (!this.isVisible)
            this.visibility = View.VISIBLE
    }
}

fun View.gone() {
    post {
        if (this.isVisible)
            this.visibility = View.GONE
    }
}

fun View.invisible() {
    post {
        if (this.isVisible)
            this.visibility = View.INVISIBLE
    }
}

fun View.enable() {
    if (!this.isEnabled)
        this.isEnabled = true
}

fun View.disable() {
    if (this.isEnabled)
        this.isEnabled = false
}

fun View.clickable() {
    if (!this.isClickable)
        this.isClickable = true
}

fun View.notClickable() {
    if (this.isClickable)
        this.isClickable = false
}

fun View.toggleLoading(isLoading: Boolean) {
    if (isLoading)
        this.visible()
    else
        this.gone()
}