package com.selfformat.goldpare.androidApp.compose.results

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
import com.selfformat.goldpare.shared.model.SortingType.NONE
import com.selfformat.goldpare.shared.model.WeightRange
import kotlinx.coroutines.launch

@SuppressWarnings("TooManyFunctions")
internal class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var data: List<GoldItem>
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))
    private var currentCoinTypeFilter = GoldCoinType.ALL
    private var currentSortingType = NONE
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

    fun updateWeightFiltering(weight: WeightRange) {
        currentWeightFilter = weight
        _state.value = loadedStateWithSortingAndFiltering()
    }

    fun updateSearchKeyword(searchedPhrase: String) {
        currentSearchPhrase = searchedPhrase
        _state.value = loadedStateWithSortingAndFiltering()
    }

    private fun loadedStateWithSortingAndFiltering() =
        State.Loaded(
            goldItems = data.searchFor(currentSearchPhrase)
                .filterGoldType(currentGoldTypeFilter)
                .filterByCoinType(currentCoinTypeFilter)
                .filterByWeight(currentWeightFilter)
                .showCoinSets(showGoldSets)
                .filterByMint(currentMint)
                .filterPriceFrom(priceFromFilter)
                .filterPriceTo(priceToFilter)
                .sortBy(currentSortingType),
            title = "Złoto" // TODO title based on args with desired filtering
        )

    fun goToFiltersScreen() {
        TODO("Not yet implemented")
    }

    sealed class State {
        data class Loaded(val goldItems: List<GoldItem>, val title: String) : State()
        data class Error(val throwable: Throwable) : State()
        object Loading : State()
    }
}
