package com.selfformat.goldpare.androidApp.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.compose.commonComposables.BottomGradient
import com.selfformat.goldpare.androidApp.compose.commonComposables.BottomNavigationBar
import com.selfformat.goldpare.androidApp.compose.commonComposables.ErrorView
import com.selfformat.goldpare.androidApp.compose.commonComposables.Loading
import com.selfformat.goldpare.androidApp.compose.filtering.FilteringView
import com.selfformat.goldpare.androidApp.compose.home.HomeLoaded
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.results.ResultsLoaded
import com.selfformat.goldpare.androidApp.compose.theme.GoldpareTheme
import com.selfformat.goldpare.shared.api.XauPln

class RootActivity : AppCompatActivity() {

    @ExperimentalLayout
    @ExperimentalAnimationApi
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

    @ExperimentalLayout
    @ExperimentalAnimationApi
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
                            is HomeViewModel.State.Home -> {
                                HomeLoaded(homeViewModel, it, xauPln)
                            }
                            is HomeViewModel.State.Error -> {
                                ErrorView(it.throwable)
                            }
                            is HomeViewModel.State.Loading -> {
                                Loading()
                            }
                            is HomeViewModel.State.ShowResults -> {
                                ResultsLoaded(homeViewModel, it, xauPln)
                            }
                            is HomeViewModel.State.Filtering -> {
                                FilteringView(homeViewModel)
                            }
                            is HomeViewModel.State.Bookmarks -> {
                            }
                            is HomeViewModel.State.Settings -> {
                            }
                        }
                    }
                }
                if (state !is HomeViewModel.State.Filtering) {
                    BottomGradient()
                    BottomNavigationBar(homeViewModel)
                }
            }
        }
    }
}
