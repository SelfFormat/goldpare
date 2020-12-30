package com.selfformat.goldpare.androidApp.compose.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.commonComposables.GoldCard
import com.selfformat.goldpare.androidApp.compose.commonComposables.ResultsSearchView
import com.selfformat.goldpare.androidApp.compose.enums.GoldType
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.theme.CHIP_ICON_SCALE
import com.selfformat.goldpare.androidApp.compose.theme.bottomNavigationHeight
import com.selfformat.goldpare.androidApp.compose.theme.dp12
import com.selfformat.goldpare.androidApp.compose.theme.dp16
import com.selfformat.goldpare.androidApp.compose.theme.dp4
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.noElevation
import com.selfformat.goldpare.androidApp.compose.util.openWebPage
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem

@ExperimentalAnimationApi
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
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalUnsignedTypes
@Composable
private fun GoldResults(
    list: List<GoldItem>,
    xauPln: XauPln,
    model: HomeViewModel,
    title: String = stringResource(R.string.gold)
) {
    val context = AmbientContext.current
    LazyColumn {
        item {
            TopBar(title)
            SortFilterCTA(model)
            ListOfAppliedFilters(model)
        }
        items(list) {
            GoldCard(it, xauPln) {
                openWebPage(
                    it.link,
                    context = context
                )
            }
        }
        item {
            Spacer(modifier = Modifier.preferredHeight(bottomNavigationHeight))
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun TopBar(title: String) {
    val homeViewModel = viewModel<HomeViewModel>()
    TopAppBar(
        title = {
            val text = remember { mutableStateOf(TextFieldValue()) }
            val textFieldFocusState = remember { mutableStateOf(false) }

            ResultsSearchView(
                textFieldValue = text.value,
                onTextFieldFocused = { focused -> textFieldFocusState.value = focused },
                focusState = textFieldFocusState.value,
                onTextChanged = { text.value = it },
                placeholderText = title,
                backgroundColor = MaterialTheme.colors.background,
                keyboardShown = false,
                searchAction = { performSearch(homeViewModel, text, textFieldFocusState) }
            )
        },
        backgroundColor = MaterialTheme.colors.background,
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

private fun performSearch(
    viewModel: HomeViewModel,
    text: MutableState<TextFieldValue>,
    textFieldFocusState: MutableState<Boolean>
) {
    if (viewModel.state.value is HomeViewModel.State.ShowResults) {
        // ensure you can call this
        viewModel.updateSearchKeyword(text.value.text)
    }
    if (textFieldFocusState.value) {
        viewModel.showResults()
    }
}

@Composable
private fun SortFilterCTA(viewModel: HomeViewModel) {
    OutlinedButton(onClick = {
        viewModel.showFiltering()
        }, border = null) {
        Image(
            vectorResource(id = R.drawable.ic_filter_sort),
            modifier = Modifier.padding(end = dp8), colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
        )
        Text(text = stringResource(R.string.filter_sort_label), color = MaterialTheme.colors.onBackground)
    }
}

@ExperimentalAnimationApi
@Suppress("LongMethod")
@Composable
private fun ListOfAppliedFilters(viewModel: HomeViewModel) {
    val appliedFilters: HomeViewModel.Filters? = viewModel.appliedFilters.observeAsState().value
    if (appliedFilters != null) {
        ScrollableRow(contentPadding = PaddingValues(start = dp16, bottom = dp16)) {
            AnimatedVisibility(visible = appliedFilters.isSortingApplied) {
                Chip(stringResource(appliedFilters.sortingType.sortingName)) {
                    viewModel.clearSortingType()
                }
            }
            appliedFilters.searchPhrase?.let {
                Chip(it) { viewModel.clearSearchKeyword() }
            }
            AnimatedVisibility(visible = appliedFilters.isGoldTypeApplied) {
                Chip(stringResource(appliedFilters.goldTypeFilter.typeName)) {
                    viewModel.clearGoldTypeFiltering()
                }
            }
            AnimatedVisibility(visible = appliedFilters.isCoinTypeApplied) {
                if (appliedFilters.goldTypeFilter != GoldType.BAR) {
                    Chip(stringResource(appliedFilters.coinTypeFilter.coinName)) {
                        viewModel.clearCoinTypeFiltering()
                    }
                }
            }
            AnimatedVisibility(visible = appliedFilters.isWeightTypeApplied) {
                Chip(stringResource(appliedFilters.weightFilter.labelRangeName)) {
                    viewModel.clearWeightFiltering()
                }
            }
            AnimatedVisibility(visible = appliedFilters.isMintTypeApplied) {
                Chip(appliedFilters.mint.fullName) {
                    viewModel.clearMintFiltering()
                }
            }
            if (appliedFilters.bothPricesApplied) {
                AnimatedVisibility(visible = appliedFilters.bothPricesApplied) {
                    Chip(stringResource(
                        R.string.price_from_to_chip,
                        appliedFilters.priceFromFilter.toFloat(),
                        appliedFilters.priceToFilter.toFloat()
                    )) {
                        viewModel.clearPriceToFiltering()
                    }
                }
            } else {
                AnimatedVisibility(visible = appliedFilters.isPriceFromApplied) {
                    Chip(stringResource(R.string.price_from_chip, appliedFilters.priceFromFilter.toFloat())) {
                        viewModel.clearPriceFromFiltering()
                    }
                }
                AnimatedVisibility(visible = appliedFilters.isPriceToApplied) {
                    Chip(stringResource(R.string.price_to_chip, appliedFilters.priceToFilter.toFloat())) {
                        viewModel.clearPriceToFiltering()
                    }
                }
            }
            AnimatedVisibility(visible = !appliedFilters.showGoldSets) {
                Chip(stringResource(R.string.hide_sets)) {
                    viewModel.clearDisplayingGoldSets()
                }
            }
        }
    }
}

@Composable
private fun Chip(text: String, onClick: (() -> Unit)) {
    Row(
        modifier = Modifier.padding(
            top = dp12,
            end = dp12
        ).clip(CircleShape).clickable(onClick = onClick).background(
            color = MaterialTheme.colors.primaryVariant
        ).wrapContentWidth().clipToBounds(),
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(end = dp4, start = dp12)
        )
        Icon(
            imageVector = Icons.Filled.Close,
            modifier = Modifier.scale(CHIP_ICON_SCALE)
        )
    }
}
