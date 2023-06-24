package com.fkexample.cointicker.utils

data class DataState<out T>(
    val data: T? = null,
    val error: Throwable? = null,
    val loading: Boolean = false,
){
    companion object{

        fun <T> success(
            data: T
        ): DataState<T>{
            return DataState(
                data = data,
            )
        }

        fun <T> error(
            error: Throwable?,
        ): DataState<T>{
            return DataState(
                error = error
            )
        }

        fun <T> loading(): DataState<T> = DataState(loading = true)
    }
}