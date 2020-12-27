package com.selfformat.goldpare.androidApp.compose.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selfformat.goldpare.androidApp.compose.util.NO_PRICE_FILTERING
import com.selfformat.goldpare.androidApp.compose.util.SHOW_GOLD_SETS
import com.selfformat.goldpare.androidApp.compose.util.filterByCoinType
import com.selfformat.goldpare.androidApp.compose.util.filterByMint
import com.selfformat.goldpare.androidApp.compose.util.filterByWeight
import com.selfformat.goldpare.androidApp.compose.util.filterGoldType
import com.selfformat.goldpare.androidApp.compose.util.filterPriceFrom
import com.selfformat.goldpare.androidApp.compose.util.filterPriceTo
import com.selfformat.goldpare.androidApp.compose.util.searchFor
import com.selfformat.goldpare.androidApp.compose.util.showCoinSets
import com.selfformat.goldpare.androidApp.compose.util.sortBy
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldCoinType
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.GoldType
import com.selfformat.goldpare.shared.model.Mint
import com.selfformat.goldpare.shared.model.SortingType
import com.selfformat.goldpare.shared.model.WeightRange
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))
    private lateinit var data: List<GoldItem>
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private var currentCoinTypeFilter = GoldCoinType.ALL
    private var currentSortingType = SortingType.NONE
    private var showGoldSets = SHOW_GOLD_SETS
    private var priceFromFilter = NO_PRICE_FILTERING
    private var priceToFilter = NO_PRICE_FILTERING
    private var currentGoldTypeFilter = GoldType.ALL
    private var currentMint = Mint.ALL
    private var currentWeightFilter = WeightRange.ALL
    private var currentSearchPhrase: String? = null

    init {
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
        currentCoinTypeFilter = goldCoinType
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updateSortingType(sortingType: SortingType) {
        currentSortingType = sortingType
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updateGoldTypeFiltering(goldType: GoldType) {
        currentGoldTypeFilter = goldType
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updateMintFiltering(mint: Mint) {
        currentMint = mint
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updateDisplayingGoldSets(show: Boolean) {
        showGoldSets = show
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updatePriceToFiltering(priceTo: Double) {
        priceToFilter = priceTo
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updatePriceFromFiltering(priceFrom: Double) {
        priceFromFilter = priceFrom
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updateWeightFiltering(weight: WeightRange) {
        currentWeightFilter = weight
        _state.value = searchResultsWithSortingAndFiltering()
    }

    fun updateSearchKeyword(searchedPhrase: String) {
        currentSearchPhrase = searchedPhrase
        _state.value = searchResultsWithSortingAndFiltering()
    }

    private fun searchResultsWithSortingAndFiltering() =
        State.ShowResults(
            goldItems = data.searchFor(currentSearchPhrase)
                .filterGoldType(currentGoldTypeFilter)
                .filterByCoinType(currentCoinTypeFilter)
                .filterByWeight(currentWeightFilter)
                .showCoinSets(showGoldSets)
                .filterByMint(currentMint)
                .filterPriceFrom(priceFromFilter)
                .filterPriceTo(priceToFilter)
                .sortBy(currentSortingType),
            title = if (currentGoldTypeFilter == GoldType.ALL) "Wyniki wyszukiwanie" else currentGoldTypeFilter.typeName
        )

    fun goToFiltersScreen() {
        TODO("Not yet implemented")
    }

    private fun loadedStateWithSortingAndFiltering() = State.Home(listOfFeaturedItems())

    private fun listOfFeaturedItems(): List<Pair<GoldItem, WeightRange>> {
        val nullableListOfFeaturedItems = listOfNotNull(
            bestAmongWeightRange(WeightRange.OZ_2),
            bestAmongWeightRange(WeightRange.OZ_1),
            bestAmongWeightRange(WeightRange.OZ_1_4),
            bestAmongWeightRange(WeightRange.OZ_1_2),
            bestAmongWeightRange(WeightRange.OZ_1_10),
        )
        return if (nullableListOfFeaturedItems.isNullOrEmpty()) emptyList() else nullableListOfFeaturedItems
    }

    private fun bestAmongWeightRange(weightRange: WeightRange): Pair<GoldItem, WeightRange>? {
        val goldItem = data.filterGoldType(GoldType.COIN)
            .filterByWeight(weightRange)
            .showCoinSets(false)
            .filter { it.priceDouble != null }
            .minByOrNull { it.priceDouble!! }
        return if (goldItem == null) {
            null
        } else {
            goldItem to weightRange
        }
    }

    fun backToHome() {
        _state.value = loadedStateWithSortingAndFiltering()
    }

    sealed class State {
        data class Home(val goldItems: List<Pair<GoldItem, WeightRange>>) : State()
        data class Error(val throwable: Throwable) : State()
        data class ShowResults(val goldItems: List<GoldItem>, val title: String) : State()
        object Loading : State()
    }
}
