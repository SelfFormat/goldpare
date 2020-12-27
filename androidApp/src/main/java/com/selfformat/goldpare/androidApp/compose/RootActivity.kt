package com.selfformat.goldpare.androidApp.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.compose.commonComposables.ErrorView
import com.selfformat.goldpare.androidApp.compose.commonComposables.Loading
import com.selfformat.goldpare.androidApp.compose.home.HomeLoaded
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.results.ResultsView
import com.selfformat.goldpare.androidApp.compose.theme.GoldpareTheme
import com.selfformat.goldpare.shared.api.XauPln

class RootActivity : AppCompatActivity() {

    @ExperimentalUnsignedTypes
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoldpareTheme {
                Surface(color = MaterialTheme.colors.background) {
                    HomeView()
                }
            }
        }
    }

    @ExperimentalUnsignedTypes
    @ExperimentalFoundationApi
    @Composable
    fun HomeView() {
        Box(Modifier.fillMaxSize()) {
            val homeViewModel: HomeViewModel = viewModel()
            val state = homeViewModel.state.observeAsState().value

            val xauPlnViewModel: XauPlnViewModel = viewModel()
            xauPlnViewModel.loadXauPln()
            val xauPln: XauPln? = xauPlnViewModel.xaupln.observeAsState().value

            if (xauPln != null) {
                Column {
                    state.let {
                        when (it) {
                            is HomeViewModel.State.Loaded -> {
                                HomeLoaded(homeViewModel, it, xauPln)
                            }
                            is HomeViewModel.State.Error -> {
                                ErrorView(it.throwable)
                            }
                            is HomeViewModel.State.Loading -> {
                                Loading()
                            }
                            is HomeViewModel.State.ShowResults -> {
                                ResultsView()
                            }
                            null -> TODO()
                        }
                    }
                }
            }
        }
    }
}
