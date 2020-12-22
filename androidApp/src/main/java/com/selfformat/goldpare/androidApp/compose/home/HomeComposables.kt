package com.selfformat.goldpare.androidApp.compose.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.zIndex
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.XauPlnViewModel
import com.selfformat.goldpare.androidApp.compose.commonComposables.ErrorView
import com.selfformat.goldpare.androidApp.compose.commonComposables.GoldCard
import com.selfformat.goldpare.androidApp.compose.commonComposables.Loading
import com.selfformat.goldpare.androidApp.compose.ui.SearchView
import com.selfformat.goldpare.androidApp.compose.ui.dp12
import com.selfformat.goldpare.androidApp.compose.ui.dp16
import com.selfformat.goldpare.androidApp.compose.ui.dp20
import com.selfformat.goldpare.androidApp.compose.ui.dp8
import com.selfformat.goldpare.androidApp.compose.ui.gradientHeight
import com.selfformat.goldpare.androidApp.compose.ui.smallFontSize
import com.selfformat.goldpare.androidApp.compose.ui.topSectionHeight
import com.selfformat.goldpare.androidApp.compose.util.openWebPage
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.WeightRange


@ExperimentalFoundationApi
@Composable
fun HomeView(viewModel: HomeViewModel) {
    Box(Modifier.fillMaxSize()) {
        viewModel.loadFeaturedGoldItems()
        val state = viewModel.state.observeAsState().value

        val xauPlnViewModel: XauPlnViewModel = viewModel()
        xauPlnViewModel.loadXauPln()
        val xauPln: XauPln? = xauPlnViewModel.xaupln.observeAsState().value

        xauPln.let { xau_to_pln ->
            if (xauPln != null) {
                Column {
                    TopSection(xau_to_pln, viewModel)
                    state.let {
                        when (it) {
                            is HomeViewModel.State.Loaded -> {
                                FeaturedGoldList(list = it.goldItems, xauPln = xau_to_pln!!)
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
                            is HomeViewModel.State.Error -> {
                                ErrorView(it.throwable)
                            }
                            is HomeViewModel.State.Loading -> {
                                Loading()
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun TopSection(xauToPln: XauPln?, viewModel: HomeViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(topSectionHeight).padding(start = dp16, end = dp16, top = dp12, bottom = dp12)
    ) {
        Image(vectorResource(id = R.drawable.ic_temp_mini_logo), modifier = Modifier.padding(end = dp16))
        val formattedXauPln = "%.2f".format(xauToPln!!.price)
        Column(
            modifier = Modifier.padding(end = dp16)
        ) {
            Text("Kurs złota", fontWeight = FontWeight.Bold, style = TextStyle.Default.copy(fontSize = smallFontSize))
            Text("$formattedXauPln zł/oz", style = TextStyle.Default.copy(fontSize = smallFontSize))
        }
        SearchView(
            viewModel = viewModel,
            function = { searchedPhrase ->
                viewModel.search(searchedPhrase)
            },
            placeholderText = "Szukaj"
        )
    }
}


@Composable
private fun FeaturedGoldList(list: List<Pair<GoldItem, WeightRange>>, xauPln: XauPln) {
    val context = AmbientContext.current
    LazyColumn {
        list.forEach {
            item {
                GoldCardWithLabel(it.first, it.second, xauPln) {
                    openWebPage(
                        it.first.link,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable()
private fun GoldCardWithLabel(item: GoldItem, weight: WeightRange, xauPln: XauPln, onClick: (() -> Unit)) {
    Box {
        WeightLabel(weight.labelRangeName)
        GoldCard(item, xauPln, onClick)
    }
}

@Composable
private fun WeightLabel(labelRangeName: String) {
    Box(modifier = Modifier.padding(top = dp20, start = dp16).zIndex(2f)) {
        Image(vectorResource(id = R.drawable.ic_label))
        Text(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = dp8),
            text = labelRangeName,
            color = Color.White,
            fontSize = smallFontSize
        )
    }
}
