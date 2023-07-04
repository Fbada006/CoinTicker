package com.fkexample.cointicker.utils

import androidx.annotation.StringRes
import com.fkexample.cointicker.R
import retrofit2.HttpException
import java.io.IOException

/**
 * Extension functions for the throwable class
 */
@StringRes
fun Throwable.getErrorMessage() = when (this) {
    is HttpException -> {
        when (this.code()) {
            404 -> R.string.resource_not_found
            429 -> R.string.too_many_requests
            500 -> R.string.server_error
            else -> R.string.something_went_wrong_network
        }
    }

    is IOException -> {
        R.string.no_internet
    }

    else -> {
        R.string.something_went_wrong_please_try_again
    }
}