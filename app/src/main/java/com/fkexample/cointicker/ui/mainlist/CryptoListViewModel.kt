package com.fkexample.cointicker.ui.mainlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fkexample.cointicker.presentation.models.Crypto
import com.fkexample.cointicker.usecases.GetAllCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(private val getAllCoinsUseCase: GetAllCoinsUseCase) : ViewModel() {

    val cryptos: MutableState<List<Crypto>> = mutableStateOf(emptyList())

    val isLoading = mutableStateOf(false)

    init {
        getAllCoins()
    }

    private fun getAllCoins() {
        getAllCoinsUseCase().onEach { dataState ->
            isLoading.value = dataState.loading

            dataState.data?.let { list ->
                cryptos.value = list
            }

            dataState.error?.let {

            }
        }.launchIn(viewModelScope)
    }
}