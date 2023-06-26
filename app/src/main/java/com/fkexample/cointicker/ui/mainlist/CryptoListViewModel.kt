package com.fkexample.cointicker.ui.mainlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fkexample.cointicker.ui.models.Crypto
import com.fkexample.cointicker.usecases.GetAllCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(private val getAllCoinsUseCase: GetAllCoinsUseCase) : ViewModel() {

    private val mutableCryptosState = MutableStateFlow(listOf<Crypto>())
    val cryptos = mutableCryptosState.asStateFlow()

    private val mutableIsLoadingState = MutableStateFlow(false)
    val isLoading = mutableIsLoadingState.asStateFlow()

    private val originalCryptoList = mutableListOf<Crypto>()

    init {
        getAllCoins()
    }

    private fun getAllCoins() {
        getAllCoinsUseCase().onEach { dataState ->
            mutableIsLoadingState.value = dataState.loading

            dataState.data?.let { list ->
                mutableCryptosState.value = list
            }

            dataState.error?.let {

            }
        }.launchIn(viewModelScope)
    }

    fun onFavouriteClick(crypto: Crypto) {
        TODO("Not yet implemented")
    }

    fun onSearch(query: String) {
        val filteredListFlow = flow {
            if (originalCryptoList.isEmpty()) {
                originalCryptoList.addAll(mutableCryptosState.value)
            }

            val filteredList = if (query.isNotEmpty()) {
                originalCryptoList.filter { it.assetId.contains(query, ignoreCase = true) }
            } else {
                originalCryptoList
            }

            emit(filteredList)
        }

        viewModelScope.launch {
            filteredListFlow.collect { filteredList ->
                mutableCryptosState.value = filteredList
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        originalCryptoList.clear()
    }
}