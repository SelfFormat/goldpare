package com.selfformat.goldpare.androidApp.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.data.GoldType
import com.selfformat.goldpare.androidApp.data.PredefinedWeightRange
import com.selfformat.goldpare.androidApp.data.PredefinedWeightRanges
import com.selfformat.goldpare.androidApp.data.WeightRange
import com.selfformat.goldpare.androidApp.theme.bottomNavigationHeight
import com.selfformat.goldpare.androidApp.theme.categoryGradientBottomDark
import com.selfformat.goldpare.androidApp.theme.categoryGradientBottomLight
import com.selfformat.goldpare.androidApp.theme.categoryGradientTopDark
import com.selfformat.goldpare.androidApp.theme.categoryGradientTopLight
import com.selfformat.goldpare.androidApp.theme.dp12
import com.selfformat.goldpare.androidApp.theme.dp14
import com.selfformat.goldpare.androidApp.theme.dp16
import com.selfformat.goldpare.androidApp.theme.dp20
import com.selfformat.goldpare.androidApp.theme.dp8
import com.selfformat.goldpare.androidApp.theme.fontWeight300
import com.selfformat.goldpare.androidApp.theme.gray500
import com.selfformat.goldpare.androidApp.theme.headerDescriptionFontSize
import com.selfformat.goldpare.androidApp.theme.headerFontSize
import com.selfformat.goldpare.androidApp.theme.shapes
import com.selfformat.goldpare.androidApp.theme.smallFontSize
import com.selfformat.goldpare.androidApp.theme.topSectionHeight
import com.selfformat.goldpare.androidApp.utils.openWebPage
import com.selfformat.goldpare.androidApp.viewModels.HomeViewModel
import com.selfformat.goldpare.shared.models.GoldItem
import com.selfformat.goldpare.shared.models.XauPln
import java.util.Locale

@ExperimentalFoundationApi
@Composable
fun HomeLoaded(
    viewModel: HomeViewModel,
    it: HomeViewModel.State.Home,
    xauPln: XauPln,
) {
    FeaturedGoldList(list = it.goldItems, xauPln = xauPln, viewModel = viewModel)
}

@ExperimentalFoundationApi
@Composable
private fun TopSection(xauToPln: XauPln?, viewModel: HomeViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(topSectionHeight).padding(start = dp16, end = dp16, top = dp12, bottom = dp12)
    ) {
        Image(
            vectorResource(id = R.drawable.ic_logo),
            modifier = Modifier.padding(end = dp16).clickable(onClick = { viewModel.fetchData(forceReload = true) }),
        )
        Column(
            modifier = Modifier.padding(end = dp16)
        ) {
            Text(
                stringResource(R.string.gold_exchange_rate_title),
                fontWeight = FontWeight.Bold,
                style = TextStyle.Default.copy(fontSize = smallFontSize)
            )
            xauToPln?.let {
                Text(
                    stringResource(R.string.xau_pln_exchange_rate, it.price),
                    style = TextStyle.Default.copy(fontSize = smallFontSize)
                )
            }
        }
        HomeFakeSearchView(
            function = {
                viewModel.clearFilters()
                viewModel.showResults(forceFocus = true)
            }
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun HeaderSection(viewModel: HomeViewModel, xauToPln: XauPln?) {
    TopSection(xauToPln, viewModel)
    Header(text = stringResource(R.string.categories_title), Modifier.padding(start = dp16, end = dp16, bottom = dp16))
    Categories(viewModel)
    Header(text = stringResource(R.string.best_price_category_title), Modifier.padding(start = dp16, end = dp16))
    HeaderDescription(stringResource(R.string.best_oz_price_category_description))
}

@ExperimentalFoundationApi
@Composable
private fun FeaturedGoldList(list: List<Pair<GoldItem, WeightRange>>, xauPln: XauPln, viewModel: HomeViewModel) {
    val context = AmbientContext.current

    LazyColumn {
        item {
            HeaderSection(viewModel = viewModel, xauToPln = xauPln)
        }
        itemsIndexed(list) { index, pair ->
            if (pair.second is PredefinedWeightRange) {
                if (index == 0) {
                    GoldCardWithLabel(
                        pair.first,
                        (pair.second as PredefinedWeightRange).predefinedWeightRanges,
                        xauPln,
                        modifier = Modifier.padding(top = dp16)
                    ) {
                        openWebPage(
                            pair.first.link,
                            context = context
                        )
                    }
                } else {
                    GoldCardWithLabel(
                        pair.first,
                        (pair.second as PredefinedWeightRange).predefinedWeightRanges,
                        xauPln,
                        modifier = Modifier
                    ) {
                        openWebPage(
                            pair.first.link,
                            context = context
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.preferredHeight(bottomNavigationHeight))
        }
    }
}

@Composable
private fun GoldCardWithLabel(
    item: GoldItem,
    predefinedWeight: PredefinedWeightRanges,
    xauPln: XauPln,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)
) {
    Box(modifier = modifier) {
        WeightLabel(stringResource(predefinedWeight.labelRangeName))
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
        Spacer(modifier = Modifier.preferredWidth(dp16))
        CategoryBox(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.coins).toUpperCase(Locale.getDefault()),
            backgroundImageId = R.drawable.coins_category
        ) {
            viewModel.clearFilters()
            viewModel.updateGoldTypeFiltering(GoldType.COIN)
            viewModel.showResults()
        }
        Spacer(modifier = Modifier.preferredWidth(dp8))
        CategoryBox(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.bars).toUpperCase(Locale.getDefault()),
            backgroundImageId = R.drawable.bars_category
        ) {
            viewModel.clearFilters()
            viewModel.updateGoldTypeFiltering(GoldType.BAR)
            viewModel.showResults()
        }
        Spacer(modifier = Modifier.preferredWidth(dp8))
        CategoryBox(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.all).toUpperCase(Locale.getDefault()),
            backgroundImageId = R.drawable.all_category
        ) {
            viewModel.clearFilters()
            viewModel.updateGoldTypeFiltering(GoldType.ALL)
            viewModel.showResults()
        }
        Spacer(modifier = Modifier.preferredWidth(dp16))
    }
}

@Composable
private fun CategoryBox(
    text: String,
    modifier: Modifier,
    @DrawableRes backgroundImageId: Int? = null,
    onClick: (() -> Unit)
) {
    val isLightTheme = MaterialTheme.colors.isLight
    val gradientTopColor = if (isLightTheme) categoryGradientTopLight else categoryGradientTopDark
    val gradientBottomColor = if (isLightTheme) categoryGradientBottomLight else categoryGradientBottomDark

    Box(
        modifier.clip(shapes.small).background(
            brush = Brush.verticalGradient(
                0.0f to gradientTopColor,
                1.0f to gradientBottomColor
            )
        ).clickable(onClick = onClick).fillMaxHeight()
    ) {
        if (backgroundImageId != null) {
            Image(imageResource(backgroundImageId))
        }
        val textAlignment = if (backgroundImageId != R.drawable.all_category) {
            Alignment.BottomCenter
        } else Alignment.Center
        Text(text = text, Modifier.padding(
            start = dp8,
            end = dp8,
            top = dp20,
            bottom = dp14
        ).wrapContentSize().align(textAlignment))
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
