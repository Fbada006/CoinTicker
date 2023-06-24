package com.fkexample.cointicker.network.utils

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val headerName: String, private val headerValue: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header(headerName, headerValue)
            .build()
        return chain.proceed(modifiedRequest)
    }
}
