package com.fkexample.cointicker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fkexample.cointicker.ui.models.Crypto
import com.fkexample.cointicker.ui.models.CryptoDetails
import com.fkexample.cointicker.usecases.AddCoinToFavoriteUseCase
import com.fkexample.cointicker.usecases.GetAllCoinsUseCase
import com.fkexample.cointicker.usecases.GetAllFavoriteCoinsUseCase
import com.fkexample.cointicker.usecases.GetCoinDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val getAllCoinsUseCase: GetAllCoinsUseCase,
    private val addCoinToFavoriteUseCase: AddCoinToFavoriteUseCase,
    private val getAllFavoriteCoinsUseCase: GetAllFavoriteCoinsUseCase,
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase
) : ViewModel() {

    private val mutableCryptosState = MutableStateFlow(listOf<Crypto>())
    val cryptos = mutableCryptosState.asStateFlow()

    private val mutableFavCryptosState = MutableStateFlow(listOf<Crypto>())
    val favCryptos = mutableFavCryptosState.asStateFlow()

    private val mutableDetailCryptosState: MutableStateFlow<CryptoDetails?> = MutableStateFlow(null)
    val detailCryptosState = mutableDetailCryptosState.asStateFlow()

    private val mutableIsLoadingState = MutableStateFlow(false)
    val isLoading = mutableIsLoadingState.asStateFlow()

    private val mutableErrorState: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val error = mutableErrorState.asStateFlow()

    // Keep track of the original loaded list before search
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

            mutableErrorState.value = dataState.error
        }.launchIn(viewModelScope)
    }

    fun getAllFavoriteCoins() {
        getAllFavoriteCoinsUseCase().onEach { dataState ->
            mutableIsLoadingState.value = dataState.loading

            dataState.data?.let { list ->
                mutableFavCryptosState.value = list
            }

            mutableErrorState.value = dataState.error
        }.launchIn(viewModelScope)
    }

    fun getCoinDetails(assetId: String) {
        getCoinDetailsUseCase(assetId).onEach { dataState ->
            mutableIsLoadingState.value = dataState.loading

            dataState.data?.let { details ->
                mutableDetailCryptosState.value = details
            }

            mutableErrorState.value = dataState.error
        }.launchIn(viewModelScope)
    }

    fun onFavouriteClick(crypto: Crypto) {
        viewModelScope.launch {
            try {
                addCoinToFavoriteUseCase(crypto)
                refreshList(crypto)
            } catch (e: Exception) {
                // We do not care about the error except to log it and the user will not get an updated UI
                Timber.e("Error saving favourite $e")
            }
        }
    }

    /**
     * This is a helper function to quickly modify the list and update the UI. Since it runs inside the try block of the
     * method to add to favorites, we can know for sure that the file has already been saved without an exception to the db when this runs.
     * Instead of querying the entire list, which makes for a bad jumping UI, we can go ahead and update the isFavourite property of the item
     * added to favorites here, which creates a better animation effect on the UI.
     *
     * @param crypto is the item clicked on the UI
     * */
    private fun refreshList(crypto: Crypto) {
        val cryptoList = mutableCryptosState.value.toMutableList()

        if (cryptoList.contains(crypto)) {
            val index = cryptoList.indexOf(crypto)

            cryptoList[index] = crypto.copy(isFavorite = !crypto.isFavorite)

            mutableCryptosState.value = cryptoList
        }
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

    fun dismissError() {
        mutableErrorState.value = null
    }
}