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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CoinState())
    val state: StateFlow<CoinState> = _state.asStateFlow()

    // Keep track of the original loaded list before search
    private val originalCryptoList = mutableListOf<Crypto>()

    init {
        getAllCoins()
    }

    /**
     * Retrieves all coins and updates the state accordingly.
     */
    private fun getAllCoins() {
        getAllCoinsUseCase().onEach { dataState ->
            _state.update {
                it.copy(isLoading = dataState.loading, error = dataState.error)
            }

            dataState.data?.let { list ->
                _state.update { it.copy(cryptos = list, isLoading = false) }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Retrieves all favorite coins and updates the state accordingly.
     */
    fun getAllFavoriteCoins() {
        getAllFavoriteCoinsUseCase().onEach { dataState ->
            _state.update {
                it.copy(isLoading = dataState.loading, error = dataState.error)
            }

            dataState.data?.let { list ->
                _state.update { it.copy(favCryptos = list, isLoading = false) }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Retrieves coin details for the specified asset ID and updates the state accordingly.
     *
     * @param assetId The asset ID of the coin to retrieve details for.
     */
    fun getCoinDetails(assetId: String) {
        getCoinDetailsUseCase(assetId).onEach { dataState ->
            _state.update {
                it.copy(isLoading = dataState.loading, error = dataState.error)
            }

            dataState.data?.let { details ->
                _state.update { it.copy(cryptoDetails = details, isLoading = false) }
            }
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
            } catch (e: Exception) {
                // We do not care about the error except to log it and the user will not get an updated UI
                Timber.e("Error saving favourite $e")
            }
        }
    }

    /**
     * Performs a search on the coin list based on the given query and updates the state accordingly.
     *
     * @param query The search query to filter the coin list.
     */
    fun onSearch(query: String) {
        val filteredListFlow = flow {
            if (originalCryptoList.isEmpty()) {
                originalCryptoList.addAll(_state.value.cryptos)
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
                _state.update { it.copy(cryptos = filteredList) }
            }
        }
    }

    /**
     * Clears the error state.
     */
    fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        originalCryptoList.clear()
    }
}

// Handles the state for the UI
data class CoinState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val cryptos: List<Crypto> = emptyList(),
    val favCryptos: List<Crypto> = emptyList(),
    val cryptoDetails: CryptoDetails? = null,
)