package com.selfformat.goldpare.androidApp.compose.results

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.compose.XauPlnViewModel
import com.selfformat.goldpare.androidApp.compose.commonComposables.ErrorView
import com.selfformat.goldpare.androidApp.compose.commonComposables.GoldCard
import com.selfformat.goldpare.androidApp.compose.commonComposables.Loading
import com.selfformat.goldpare.androidApp.compose.theme.gradientHeight
import com.selfformat.goldpare.androidApp.compose.util.openWebPage
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
fun ResultsView() {
    Box(Modifier.fillMaxSize()) {
        val viewModel: ResultViewModel = viewModel()
        viewModel.loadGoldItems()
        val state = viewModel.state.observeAsState().value

        val xauPlnViewModel: XauPlnViewModel = viewModel()
        xauPlnViewModel.loadXauPln()
        val xauPln: XauPln? = xauPlnViewModel.xaupln.observeAsState().value
        xauPln.let { xau_to_pln ->
            if (xauPln != null) {
                Column {
                    TopBar()
                    SortFilterCTA()
                    ListOfAppliedFilters()
                    state.let {
                        when (it) {
                            is ResultViewModel.State.Loaded -> {
                                GoldList(list = it.goldItems, xauPln = xau_to_pln!!)
                                Row(
                                    modifier = Modifier
                                        .height(gradientHeight)
                                        .fillMaxWidth()
                                        .background(
                                            brush = Brush.verticalGradient(
                                                0.0f to Color.Transparent,
                                                1.0f to Color.White // TODO Fix darkmode
                                            )
                                        ),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    // This is gradient overlay effect from the bottom of the screen
                                }
                            }
                            is ResultViewModel.State.Error -> {
                                ErrorView(it.throwable)
                            }
                            is ResultViewModel.State.Loading -> {
                                Loading()
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalUnsignedTypes
@Composable
private fun GoldList(list: List<GoldItem>, xauPln: XauPln) {
    val context = AmbientContext.current
    LazyColumn {
        list.forEach {
            item {
                GoldCard(it, xauPln) {
                    openWebPage(
                        it.link,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar() {
    Row {}
}

@Composable
private fun SortFilterCTA() {
    Row {}
}

@Composable
private fun ListOfAppliedFilters() {
    Row {}
}
