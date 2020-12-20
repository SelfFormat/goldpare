package com.selfformat.goldpare.androidApp.compose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldCoinType
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.GoldType
import com.selfformat.goldpare.shared.model.Mint
import com.selfformat.goldpare.shared.model.SortingType
import com.selfformat.goldpare.shared.model.SortingType.NONE
import com.selfformat.goldpare.shared.model.SortingType.PRICE_ASC
import com.selfformat.goldpare.shared.model.SortingType.PRICE_DESC
import com.selfformat.goldpare.shared.model.SortingType.PRICE_PER_OZ_ASC
import com.selfformat.goldpare.shared.model.SortingType.PRICE_PER_OZ_DESC
import com.selfformat.goldpare.shared.model.WeightRanges
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
internal class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var data: List<GoldItem>
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val _xaupln = MutableLiveData<XauPln>()
    val xaupln: LiveData<XauPln> = _xaupln

    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))
    private var currentCoinTypeFilter = GoldCoinType.ALL
    private var currentSortingType = NONE
    private var showGoldSets = SHOW_GOLD_SETS
    private var priceFromFilter = NO_PRICE_FILTERING
    private var priceToFilter = NO_PRICE_FILTERING
    private var currentGoldTypeFilter = GoldType.ALL
    private var currentMint = Mint.all
    private var currentWeightFilter = WeightRanges.ALL
    private var currentSearchPhrase: String? = null

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

    fun loadXauPln() {
        viewModelScope.launch {
            kotlin.runCatching {
                sdk.getXauPln(false)
            }.onSuccess {
                _xaupln.value = it[0]
            }
        }
    }

    fun updateCoinTypeFiltering(goldCoinType: GoldCoinType) {
        currentCoinTypeFilter = goldCoinType
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

    fun updatePriceToFiltering(priceTo: Double) {
        priceToFilter = priceTo
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updatePriceFromFiltering(priceFrom: Double) {
        priceFromFilter = priceFrom
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updateWeightFiltering(weight: WeightRanges) {
        currentWeightFilter = weight
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updateSearchKeyword(searchedPhrase: String) {
        currentSearchPhrase = searchedPhrase
        _state.value = loadedStateWithSortingAndFiltering()
    }

    private fun List<GoldItem>.filterByCoinType(goldCoinType: GoldCoinType): List<GoldItem> {
        if (goldCoinType == GoldCoinType.ALL) return this
        return this.filter {
            it.title.contains(goldCoinType.name, ignoreCase = true)
        }
    }

    @Suppress("MaxLineLength")
    private fun List<GoldItem>.filterPriceFrom(priceFrom: Double): List<GoldItem> {
        if (priceFrom == NO_PRICE_FILTERING) return this
        return this
            .filter { it.priceDouble != null } // first filter out items which doesn't have calculated double from price string
            .filter { it.priceDouble!! >= priceFrom }
    }

    @Suppress("MaxLineLength")
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
        return this.filter { it.type == type.typeName }
    }

    private fun List<GoldItem>.filterByWeight(weight: WeightRanges): List<GoldItem> {
        if (currentWeightFilter == WeightRanges.ALL) return this
        return this
            .filter { it.weightInGrams != null } // first filter out items which doesn't have calculated weight in grams
            .filter { it.weightInGrams!! >= weight.weightFromInGrams && it.weightInGrams!! <= weight.weightToInGrams }
    }

    private fun List<GoldItem>.filterByMint(currentMint: Mint): List<GoldItem> {
        if (currentMint == Mint.all) return this
        return this.filter { it.website == currentMint.name }
    }

    private fun List<GoldItem>.searchFor(phrase: String?): List<GoldItem> {
        if (phrase.isNullOrEmpty()) return this
        return this.filter {
            it.title.contains(phrase, ignoreCase = true)
        }
    }

    private fun loadedStateWithSortingAndFiltering() =
        State.Loaded(
            data.searchFor(currentSearchPhrase).filterGoldType(currentGoldTypeFilter)
                .filterByCoinType(currentCoinTypeFilter)
                .filterByWeight(currentWeightFilter)
                .showCoinSets(showGoldSets).filterByMint(currentMint)
                .filterPriceFrom(priceFromFilter).filterPriceTo(priceToFilter)
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
