package com.selfformat.goldpare.androidApp.compose.results

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.commonComposables.BottomGradient
import com.selfformat.goldpare.androidApp.compose.commonComposables.GoldCard
import com.selfformat.goldpare.androidApp.compose.commonComposables.ResultsSearchView
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.noElevation
import com.selfformat.goldpare.androidApp.compose.util.openWebPage
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
fun ResultsLoaded(homeViewModel: HomeViewModel, state: HomeViewModel.State.ShowResults, xauPln: XauPln) {
    Box(Modifier.fillMaxSize()) {
        Column {
            GoldResults(
                list = state.goldItems,
                xauPln = xauPln,
                model = homeViewModel,
                title = state.title
            )
            BottomGradient()
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalUnsignedTypes
@Composable
private fun GoldResults(
    list: List<GoldItem>,
    xauPln: XauPln,
    model: HomeViewModel,
    title: String = "Złoto"
) {
    val context = AmbientContext.current
    LazyColumn {
        item {
            TopBar(title)
            SortFilterCTA(model)
            ListOfAppliedFilters()
        }
        items(list) {
            GoldCard(it, xauPln) {
                openWebPage(
                    it.link,
                    context = context
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun TopBar(title: String) {
    val homeViewModel = viewModel<HomeViewModel>()
    TopAppBar(
        title = {
            ResultsSearchView(
                viewModel = homeViewModel,
                placeholderText = title
            )
        },
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(
                onClick = { homeViewModel.backToHome() },
                content = {
                    Icon(Icons.Filled.ArrowBack)
                }
            )
        },
        elevation = noElevation
    )
}

@Composable
private fun SortFilterCTA(viewModel: HomeViewModel) {

    OutlinedButton(onClick = { viewModel.goToFiltersScreen() }, border = null) {
        Image(
            vectorResource(id = R.drawable.ic_filter_sort),
            modifier = Modifier.padding(end = dp8)
        )
        Text(text = "FILTRUJ / SORTUJ", color = Color.Black)
    }
}

@Composable
private fun ListOfAppliedFilters() {
    Row {}
}
