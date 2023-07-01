package com.fkexample.cointicker.utils

/**
 * A generic data class representing the state of an operation with optional data, error, and loading indicators.
 * @property data The data result of the operation.
 * @property error The error that occurred during the operation.
 * @property loading Indicates whether the operation is in progress.
 */
data class DataState<out T>(
    val data: T? = null,
    val error: Throwable? = null,
    val loading: Boolean = false,
) {
    companion object {

        /**
         * Creates a success state with the specified data.
         * @param data The data result.
         * @return A DataState instance representing the success state with data.
         */
        fun <T> success(data: T): DataState<T> {
            return DataState(data = data)
        }

        /**
         * Creates an error state with the specified error.
         * @param error The error that occurred.
         * @return A DataState instance representing the error state with the error.
         */
        fun <T> error(error: Throwable?): DataState<T> {
            return DataState(error = error)
        }

        /**
         * Creates a loading state.
         * @return A DataState instance representing the loading state.
         */
        fun <T> loading(): DataState<T> = DataState(loading = true)
    }
}
