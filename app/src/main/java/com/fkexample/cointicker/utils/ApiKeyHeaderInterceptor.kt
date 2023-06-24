package com.fkexample.cointicker.utils

import com.fkexample.cointicker.BuildConfig
import com.fkexample.cointicker.network.utils.HeaderInterceptor

object ApiKeyHeaderInterceptor {
    fun create() = HeaderInterceptor(headerName = "X-CoinAPI-Key", headerValue = BuildConfig.coinApiKey)
}