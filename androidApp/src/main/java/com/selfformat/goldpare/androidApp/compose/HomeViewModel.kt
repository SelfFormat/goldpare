package com.selfformat.goldpare.androidApp.compose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selfformat.goldpare.androidApp.compose.GoldCoinType.ALL
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var data: List<GoldItem>
    private val _state = MutableLiveData<State>()
    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))
    private var currentSorting = ALL

    val state: LiveData<State> = _state

    fun loadGoldItems() {
        viewModelScope.launch {
            kotlin.runCatching {
                sdk.getGoldItems(false)
            }.onSuccess {
                data = it
                _state.value = State.Loaded(data.filterByCoinType(currentSorting))
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

    fun updateCoinTypeFiltering(goldCoinType: GoldCoinType) {
        currentSorting = goldCoinType
        _state.value = State.Loaded(data.filterByCoinType(currentSorting))
    }

    sealed class State {
        data class Loaded(val goldItems: List<GoldItem>) : State()
        data class Error(val throwable: Throwable) : State()
        object Loading : State()
    }

}