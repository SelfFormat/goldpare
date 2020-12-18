package com.selfformat.goldpare.androidApp.compose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selfformat.goldpare.androidApp.compose.GoldCoinType.ALL
import com.selfformat.goldpare.androidApp.compose.SortingType.*
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem
import kotlinx.coroutines.launch

internal class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var data: List<GoldItem>
    private val _state = MutableLiveData<State>()
    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))
    private var currentCoinTypeFiltering = ALL
    private var currentSortingType = NONE

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

    private fun List<GoldItem>.filterByCoinType(goldCoinType: GoldCoinType) : List<GoldItem> {
        if (goldCoinType == ALL) return this
        return this.filter {
            it.title.contains(goldCoinType.name, ignoreCase = true)
        }
    }

    private fun List<GoldItem>.sortBy(sortingType: SortingType) : List<GoldItem> {
        return when (sortingType) {
            PRICE_ASC -> this.sortedBy { it.price }
            PRICE_DESC -> this.sortedByDescending { it.price }
            PRICE_PER_OZ_ASC -> this.sortedBy { it.pricePerOunce }
            PRICE_PER_OZ_DESC -> this.sortedByDescending { it.pricePerOunce }
            NONE -> this
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

    private fun loadedStateWithSortingAndFiltering() =
        State.Loaded(data.filterByCoinType(currentCoinTypeFiltering).sortBy(currentSortingType))

    sealed class State {
        data class Loaded(val goldItems: List<GoldItem>) : State()
        data class Error(val throwable: Throwable) : State()
        object Loading : State()
    }

}