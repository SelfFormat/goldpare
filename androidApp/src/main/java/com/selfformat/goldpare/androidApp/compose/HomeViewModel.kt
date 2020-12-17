package com.selfformat.goldpare.androidApp.compose

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val goldItems = MutableLiveData<List<GoldItem>>()
    private val mainScope = MainScope()
    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))

    val getGoldItem: LiveData<List<GoldItem>> = goldItems

    fun loadGoldItems() {
        mainScope.launch {
            kotlin.runCatching {
                sdk.getGoldItems(true)
            }.onSuccess {
                goldItems.postValue(it)
            }.onFailure {
                Log.i("viewmodel", "loadGoldItems: onFailure")
            }
        }
    }
}