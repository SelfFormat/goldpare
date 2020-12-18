package com.selfformat.goldpare.androidApp.compose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<State>()
    private val mainScope = MainScope()
    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))

    val state: LiveData<State> = _state

    fun loadGoldItems() {
        mainScope.launch {
            kotlin.runCatching {
                sdk.getGoldItems(true)
            }.onSuccess {
                _state.value = State.Loaded(it)
            }.onFailure {
                _state.value = State.Error(it)
            }
        }
    }

    sealed class State {
        data class Loaded(val goldItems: List<GoldItem>): State()
        data class Error(val throwable: Throwable) : State()
        object Loading : State()
    }

}