package com.dzakdzaks.laporanbendahara.utils

sealed class Resource<T> {

    data class Loading<T>(
        val isLoading: Boolean
    ) : Resource<T>()

    data class Success<T>(
        val message: String?,
        val data: T
    ) : Resource<T>()

    data class Error<T>(
        val errorData: String
    ) : Resource<T>()
}