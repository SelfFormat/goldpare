package com.selfformat.goldpare.androidApp.compose.ui

import android.graphics.Typeface
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.zIndex
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.GlideSuperImage
import com.selfformat.goldpare.androidApp.compose.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.ResultViewModel
import com.selfformat.goldpare.androidApp.compose.XauPlnViewModel
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
                                            brush = verticalGradient(
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
                                            brush = verticalGradient(
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

@Composable
private fun ErrorView(throwable: Throwable) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Error: $throwable",
        textAlign = TextAlign.Center
    )
}

@Composable
private fun SplashWithLoading() {
    Column(
        modifier = Modifier.fillMaxSize().fillMaxHeight().background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = logoFontSize,
            fontFamily = fontFamily(Typeface.SERIF)
        )
        Text("Loading...", color = Color.White, fontSize = logoDescriptionFontSize)
    }
}

@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Loading...")
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

@Composable
private fun GoldCard(item: GoldItem, xauPln: XauPln, onClick: (() -> Unit)) {
    Card(
        elevation = cardElevation,
        shape = RoundedCornerShape(cardCorners),
        modifier = Modifier
            .padding(top = dp4, bottom = dp4, start = dp16, end = dp16)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        val formattedWeightInGrams = "%.2f".format(item.weightInGrams)
        val formattedPriceDouble = "%.2f".format(item.priceDouble)
        val formattedPricePerOunce = "%.2f".format(item.pricePerOunce)
        val formattedPriceMarkup = "%.2f".format(item.priceMarkupInPercentage(xauPln.price))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.padding(dp12)) {
                if (item.image != null) {
                    GlideSuperImage(
                        item.image!!,
                        modifier = Modifier.width(imageWidth)
                    )
                }
            }
            Column(Modifier.padding(dp16)) {
                Text(text = item.title, fontWeight = FontWeight.Bold)
                Divider(color = dividerColor, thickness = dividerThickness)
                Row {
                    Text(
                        text = "$formattedPriceDouble zł",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = dp16)
                    )
                    Text(
                        text = "($formattedPricePerOunce/oz)",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(vectorResource(id = R.drawable.ic_gram), modifier = Modifier.padding(end = dp8))
                    Text(text = "$formattedWeightInGrams g", Modifier.padding(end = dp16))
                    Text(text = "marża: $formattedPriceMarkup%")
                }
                Text(text = "Mennica: ${item.mintFullName}")
                if (item.quantity > 1) {
                    Text(text = "sztuk w zestawie: ${item.quantity}")
                }
            }
        }
    }
}
