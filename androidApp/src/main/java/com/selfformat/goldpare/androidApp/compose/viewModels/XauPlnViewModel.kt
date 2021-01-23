package com.selfformat.goldpare.androidApp.compose.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.models.XauPln
import com.selfformat.goldpare.shared.database.DatabaseDriverFactory
import kotlinx.coroutines.launch

internal class XauPlnViewModel(application: Application) : AndroidViewModel(application) {

    private val _xaupln = MutableLiveData<XauPln>()
    val xaupln: LiveData<XauPln> = _xaupln

    private val sdk = GoldSDK(DatabaseDriverFactory(context = application))

    fun loadXauPln() {
        viewModelScope.launch {
            kotlin.runCatching {
                sdk.getXauPln(false)
            }.onSuccess {
                _xaupln.value = it[0]
            }
        }
    }
}
