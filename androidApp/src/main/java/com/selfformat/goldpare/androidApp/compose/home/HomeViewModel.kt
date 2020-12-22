package com.selfformat.goldpare.androidApp.compose.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selfformat.goldpare.androidApp.compose.util.filterByWeight
import com.selfformat.goldpare.androidApp.compose.util.filterGoldType
import com.selfformat.goldpare.androidApp.compose.util.showCoinSets
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.GoldType
import com.selfformat.goldpare.shared.model.WeightRange
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))
    private lateinit var data: List<GoldItem>
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state
    private val _showResults = MutableLiveData<FragState>()
    val showResults: LiveData<FragState> = _showResults

    fun loadFeaturedGoldItems() {
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

    private fun loadedStateWithSortingAndFiltering() = State.Loaded(listOfFeaturedItems())

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
            .maxByOrNull { it.priceDouble!! }
        return if (goldItem == null) {
            null
        } else {
            goldItem to weightRange
        }
    }

    fun search(searchedPhrase: String? = null) {
        _showResults.value = FragState.Loaded(true)
    }

    sealed class State {
        data class Loaded(val goldItems: List<Pair<GoldItem, WeightRange>>) : State()
        data class Error(val throwable: Throwable) : State()
        object Loading : State()
    }

    sealed class FragState {
        data class Loaded(val showResult: Boolean) : FragState()
    }
}
