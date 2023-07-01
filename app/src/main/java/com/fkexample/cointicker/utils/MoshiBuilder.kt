package com.fkexample.cointicker.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Utility object for creating an instance of Moshi JSON library.
 */
object MoshiBuilder {

    /**
     * Creates and configures a Moshi instance with KotlinJsonAdapterFactory.
     * @return An instance of Moshi.
     */
    fun create(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
}
