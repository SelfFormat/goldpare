package com.selfformat.goldpare.androidApp.compose.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSizeConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.selfformat.goldpare.androidApp.compose.commonComposables.HomeSearchView
import com.selfformat.goldpare.androidApp.compose.theme.categoryBoxMinSize
import com.selfformat.goldpare.androidApp.compose.theme.categoryGradientBottom
import com.selfformat.goldpare.androidApp.compose.theme.categoryGradientTop
import com.selfformat.goldpare.androidApp.compose.theme.dp12
import com.selfformat.goldpare.androidApp.compose.theme.dp16
import com.selfformat.goldpare.androidApp.compose.theme.dp20
import com.selfformat.goldpare.androidApp.compose.theme.dp4
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.fontWeight300
import com.selfformat.goldpare.androidApp.compose.theme.gradientHeight
import com.selfformat.goldpare.androidApp.compose.theme.gray500
import com.selfformat.goldpare.androidApp.compose.theme.headerDescriptionFontSize
import com.selfformat.goldpare.androidApp.compose.theme.headerFontSize
import com.selfformat.goldpare.androidApp.compose.theme.smallFontSize
import com.selfformat.goldpare.androidApp.compose.theme.topSectionHeight
import com.selfformat.goldpare.androidApp.compose.util.openWebPage
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.WeightRange

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
fun HomeView(viewModel: HomeViewModel) {
    Box(Modifier.fillMaxSize()) {
        viewModel.loadFeaturedGoldItems()
        val state = viewModel.state.observeAsState().value

        val xauPlnViewModel: XauPlnViewModel = viewModel()
        xauPlnViewModel.loadXauPln()
        val xauPln: XauPln? = xauPlnViewModel.xaupln.observeAsState().value

        if (xauPln != null) {
            Column {
                state.let {
                    when (it) {
                        is HomeViewModel.State.Loaded -> {
                            HomeLoaded(viewModel, it, xauPln)
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

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
private fun HomeLoaded(
    viewModel: HomeViewModel,
    it: HomeViewModel.State.Loaded,
    xauPln: XauPln,
) {
    FeaturedGoldList(list = it.goldItems, xauPln = xauPln, viewModel = viewModel)
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
        HomeSearchView(
            viewModel = viewModel,
            function = { searchedPhrase ->
                viewModel.search(searchedPhrase)
            },
            placeholderText = "Szukaj"
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun HeaderSection(viewModel: HomeViewModel, xauToPln: XauPln?) {
    TopSection(xauToPln, viewModel)
    Header(text = "Kategorie", Modifier.padding(start = dp16, end = dp16, bottom = dp16))
    Categories(viewModel)
    Header(text = "Najlepsza cena", Modifier.padding(start = dp16, end = dp16))
    HeaderDescription("w przeliczeniu za uncję dla:")
}

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
private fun FeaturedGoldList(list: List<Pair<GoldItem, WeightRange>>, xauPln: XauPln, viewModel: HomeViewModel) {
    val context = AmbientContext.current

    LazyColumn {
        item {
            HeaderSection(viewModel = viewModel, xauToPln = xauPln)
        }
        itemsIndexed(list) { index, pair ->
            if (index == 0) {
                GoldCardWithLabel(
                    pair.first,
                    pair.second,
                    xauPln,
                    modifier = Modifier.padding(top = dp16)
                ) {
                    openWebPage(
                        pair.first.link,
                        context = context
                    )
                }
            }
            GoldCardWithLabel(pair.first, pair.second, xauPln, modifier = Modifier) {
                openWebPage(
                    pair.first.link,
                    context = context
                )
            }
        }
    }
}

@ExperimentalUnsignedTypes
@Composable
private fun GoldCardWithLabel(
    item: GoldItem,
    weight: WeightRange,
    xauPln: XauPln,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)
) {
    Box(modifier = modifier) {
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

@Composable
private fun Categories(viewModel: HomeViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth().padding(bottom = dp16)
    ) {
        CategoryBox("ALL") { viewModel.search("All") }
        CategoryBox("MONETY") { viewModel.search("Monety") }
        CategoryBox("SZTABKI") { viewModel.search("Sztabki") }
    }
}

@Composable
private fun CategoryBox(text: String, onClick: (() -> Unit)) {
    Box(
        Modifier.background(
            brush = Brush.verticalGradient(
                0.0f to categoryGradientTop,
                1.0f to categoryGradientBottom // TODO add another color set for darkmode
            ),
            RoundedCornerShape(dp4),
        ).clickable(onClick = onClick).defaultMinSizeConstraints(categoryBoxMinSize)
    ) {
        Text(text = text, Modifier.padding(dp20).wrapContentSize().align(Alignment.BottomCenter))
    }
}

@Composable
private fun Header(text: String, modifier: Modifier) {
    Row(modifier = modifier) {
        Text(
            text = text,
            fontSize = headerFontSize,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun HeaderDescription(text: String) {
    Row(Modifier.padding(start = dp16, end = dp16)) {
        Text(
            text = text,
            fontSize = headerDescriptionFontSize,
            color = gray500,
            fontWeight = fontWeight300
        )
    }
}
