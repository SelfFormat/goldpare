package com.selfformat.goldpare.androidApp.compose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selfformat.goldpare.androidApp.compose.SortingType.*
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.Mint
import kotlinx.coroutines.launch

internal class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var data: List<GoldItem>
    private val _state = MutableLiveData<State>()
    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))
    private var currentCoinTypeFiltering = GoldCoinType.ALL
    private var currentSortingType = NONE
    private var showGoldSets = SHOW_GOLD_SETS
    private var priceFromFiltering = NO_PRICE_FILTERING
    private var priceToFiltering = NO_PRICE_FILTERING
    private var currentGoldTypeFilter = GoldType.ALL
    private var currentMint = Mint.all

    val state: LiveData<State> = _state

    fun loadGoldItems() {
        _state.value = State.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                sdk.getGoldItems(false)
            }.onSuccess {
                data = it
                _state.value = loadedStateWithSortingAndFiltering()
            }.onFailure {
                _state.value = State.Error(it)
            }
        }
    }

    fun updateCoinTypeFiltering(goldCoinType: GoldCoinType) {
        currentCoinTypeFiltering = goldCoinType
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updateSortingType(sortingType: SortingType) {
        currentSortingType = sortingType
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updateGoldTypeFiltering(goldType: GoldType) {
        currentGoldTypeFilter = goldType
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updateMintFiltering(mint: Mint) {
        currentMint = mint
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updateDisplayingGoldSets(show: Boolean) {
        showGoldSets = show
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun clearPriceFiltering() {
        priceFromFiltering = NO_PRICE_FILTERING
        priceToFiltering = NO_PRICE_FILTERING
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updatePriceToFiltering(priceTo: Double) {
        priceToFiltering = priceTo
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updatePriceFromFiltering(priceFrom: Double) {
        priceFromFiltering = priceFrom
        _state.value = loadedStateWithSortingAndFiltering()
    }

    private fun List<GoldItem>.filterByCoinType(goldCoinType: GoldCoinType): List<GoldItem> {
        if (goldCoinType == GoldCoinType.ALL) return this
        return this.filter {
            it.title.contains(goldCoinType.name, ignoreCase = true)
        }
    }

    private fun List<GoldItem>.filterPriceFrom(priceFrom: Double): List<GoldItem> {
        if (priceFrom == NO_PRICE_FILTERING) return this
        return this
            .filter { it.priceDouble != null } // first filter out items which doesn't have calculated double from price string
            .filter { it.priceDouble!! >= priceFrom }
    }

    private fun List<GoldItem>.filterPriceTo(priceTo: Double): List<GoldItem> {
        if (priceTo == NO_PRICE_FILTERING) return this
        return this
            .filter { it.priceDouble != null } // first filter out items which doesn't have calculated double from price string
            .filter { it.priceDouble!! <= priceTo }
    }

    private fun List<GoldItem>.showCoinSets(show: Boolean): List<GoldItem> {
        return if (show) {
            return this
        } else {
            this.filter {
                it.quantity == SINGLE_ITEM
            }
        }
    }

    private fun List<GoldItem>.sortBy(sortingType: SortingType): List<GoldItem> {
        return when (sortingType) {
            PRICE_ASC -> this.sortedBy { it.price }
            PRICE_DESC -> this.sortedByDescending { it.price }
            PRICE_PER_OZ_ASC -> this.sortedBy { it.pricePerOunce }
            PRICE_PER_OZ_DESC -> this.sortedByDescending { it.pricePerOunce }
            NONE -> this
        }
    }

    private fun List<GoldItem>.filterGoldType(type: GoldType): List<GoldItem> {
        if (currentGoldTypeFilter == GoldType.ALL) return this
        return this
            .filter { it.type == type.typeName }
    }

    private fun List<GoldItem>.filterByMint(currentMint: Mint): List<GoldItem> {
        if (currentMint == Mint.all) return this
        return this
            .filter { it.website == currentMint.name }
    }

    private fun loadedStateWithSortingAndFiltering() =
        State.Loaded(
            data.filterGoldType(currentGoldTypeFilter).filterByCoinType(currentCoinTypeFiltering)
                .showCoinSets(showGoldSets).filterByMint(currentMint)
                .filterPriceFrom(priceFromFiltering).filterPriceTo(priceToFiltering)
                .sortBy(currentSortingType)
        )

    sealed class State {
        data class Loaded(val goldItems: List<GoldItem>) : State()
        data class Error(val throwable: Throwable) : State()
        object Loading : State()
    }

    companion object {
        private const val NO_PRICE_FILTERING = -1.0
        private const val SHOW_GOLD_SETS = true
        private const val SINGLE_ITEM = 1L
    }
}
