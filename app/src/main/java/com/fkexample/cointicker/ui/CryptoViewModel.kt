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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class CryptoState(
    val isLoading:Boolean = false,
    val error:Throwable? = null,
    val cryptoList:List<Crypto> = emptyList()
)
/**
 * ViewModel class for the app. This is not a big app so this should suffice although we would use more viewmodels if the app was bigger
 *
 * @property getAllCoinsUseCase The use case for retrieving all coins.
 * @property addCoinToFavoriteUseCase The use case for adding or removing a coin from favorites.
 * @property getAllFavoriteCoinsUseCase The use case for retrieving all favorite coins.
 * @property getCoinDetailsUseCase The use case for retrieving coin details.
 */
@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val getAllCoinsUseCase: GetAllCoinsUseCase,
    private val addCoinToFavoriteUseCase: AddCoinToFavoriteUseCase,
    private val getAllFavoriteCoinsUseCase: GetAllFavoriteCoinsUseCase,
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CryptoState())
    val state:StateFlow<CryptoState> = _state.asStateFlow()

//    private val mutableCryptosState = MutableStateFlow(listOf<Crypto>())
//    val cryptos = mutableCryptosState.asStateFlow() // Flow for all the cryptos on the main screen

    private val mutableFavCryptosState = MutableStateFlow(listOf<Crypto>())
    val favCryptos = mutableFavCryptosState.asStateFlow() // Flow for all the cryptos on the fav screen

    private val mutableDetailCryptosState: MutableStateFlow<CryptoDetails?> = MutableStateFlow(null)
    val detailCryptosState = mutableDetailCryptosState.asStateFlow() // Flow for all the cryptos on the details screen

    private val mutableIsLoadingState = MutableStateFlow(false)
    val isLoading = mutableIsLoadingState.asStateFlow() // Flow for all the loading values

    private val mutableErrorState: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val error = mutableErrorState.asStateFlow() // Flow for all the errors

    // Keep track of the original loaded list before search
    private val originalCryptoList = mutableListOf<Crypto>()

    init {
        getAllCoins()
    }

    /**
     * Retrieves all coins and updates the [isLoading] [cryptos] and [error] states accordingly.
     */
    private fun getAllCoins() {
        getAllCoinsUseCase().onEach { dataState ->
            _state.value = _state.value.copy(
             isLoading = dataState.loading
            )

            dataState.data?.let { list ->
                _state.value = _state.value.copy(
                    cryptoList = list
                )
            }

            _state.value = _state.value.copy(
                error = dataState.error
            )
        }.launchIn(viewModelScope)
    }

    /**
     * Retrieves all favorite coins and updates the [isLoading]  [favCryptos] and [error] states accordingly.
     */
    fun getAllFavoriteCoins() {
        getAllFavoriteCoinsUseCase().onEach { dataState ->
            mutableIsLoadingState.value = dataState.loading

            dataState.data?.let { list ->
                mutableFavCryptosState.value = list
            }

            mutableErrorState.value = dataState.error
        }.launchIn(viewModelScope)
    }

    /**
     * Retrieves coin details for the specified asset ID and updates the [isLoading]  [detailCryptosState] and [error] states accordingly.
     *
     * @param assetId The asset ID of the coin to retrieve details for.
     */
    fun getCoinDetails(assetId: String) {
        getCoinDetailsUseCase(assetId).onEach { dataState ->
            mutableIsLoadingState.value = dataState.loading

            dataState.data?.let { details ->
                mutableDetailCryptosState.value = details
            }

            mutableErrorState.value = dataState.error
        }.launchIn(viewModelScope)
    }

    /**
     * Handles the click event on the favorite button of a coin.
     * Adds or removes the coin from favorites and updates the UI accordingly.
     *
     * @param crypto The coin to add or remove from favorites.
     */
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
     * @param crypto The item clicked on the UI.
     */
    private fun refreshList(crypto: Crypto) {
        val cryptoList = _state.value.cryptoList.toMutableList()

        if (cryptoList.contains(crypto)) {
            val index = cryptoList.indexOf(crypto)

            cryptoList[index] = crypto.copy(isFavorite = !crypto.isFavorite)

            _state.value = CryptoState(
                cryptoList = cryptoList
            )
        }
    }

    /**
     * Performs a search on the coin list based on the given query and updates the [cryptos] state accordingly.
     *
     * @param query The search query to filter the coin list.
     */
    fun onSearch(query: String) {
        val filteredListFlow = flow {
            if (originalCryptoList.isEmpty()) {
                originalCryptoList.addAll(_state.value.cryptoList)
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
                _state.value = CryptoState(
                    cryptoList = filteredList
                )
            }
        }
    }

    /**
     * Clears the error state.
     */
    fun dismissError() {
        mutableErrorState.value = null
    }

    override fun onCleared() {
        super.onCleared()
        originalCryptoList.clear()
    }
}