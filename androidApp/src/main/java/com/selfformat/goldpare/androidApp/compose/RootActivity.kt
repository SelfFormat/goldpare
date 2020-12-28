package com.selfformat.goldpare.androidApp.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.compose.commonComposables.ErrorView
import com.selfformat.goldpare.androidApp.compose.commonComposables.Loading
import com.selfformat.goldpare.androidApp.compose.filtering.FilteringView
import com.selfformat.goldpare.androidApp.compose.home.HomeLoaded
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.results.ResultsLoaded
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
                        }
                    }
                }
                if (state !is HomeViewModel.State.Filtering) {
                    BottomNavigation()
                }
            }
        }
    }

    @Composable
    private fun BottomNavigation() {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            BottomNavigation(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                contentColor = Color.Gray
            ) {
                BottomNavigationItem(icon = {
                    Icon(Icons.Filled.Home)
                }, selected = true, onClick = { /*TODO*/ })
                BottomNavigationItem(icon = {
                    Icon(Icons.Filled.FilterList)
                }, selected = true, onClick = { /*TODO*/ })
                BottomNavigationItem(icon = {
                    Icon(Icons.Filled.Notifications)
                }, selected = true, onClick = { /*TODO*/ })
                BottomNavigationItem(icon = {
                    Icon(Icons.Filled.Settings)
                }, selected = true, onClick = { /*TODO*/ })
            }
        }
    }
}
