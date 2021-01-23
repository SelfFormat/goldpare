package com.selfformat.goldpare.androidApp.compose.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.composables.Chip
import com.selfformat.goldpare.androidApp.compose.composables.GoldCard
import com.selfformat.goldpare.androidApp.compose.composables.ResultsSearchView
import com.selfformat.goldpare.androidApp.compose.data.CustomWeightRange
import com.selfformat.goldpare.androidApp.compose.data.GoldType
import com.selfformat.goldpare.androidApp.compose.data.PredefinedWeightRange
import com.selfformat.goldpare.androidApp.compose.theme.bottomNavigationHeight
import com.selfformat.goldpare.androidApp.compose.theme.dp16
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.noElevation
import com.selfformat.goldpare.androidApp.compose.utils.openWebPage
import com.selfformat.goldpare.androidApp.compose.viewModels.HomeViewModel
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ResultsLoaded(homeViewModel: HomeViewModel, state: HomeViewModel.State.ShowResults, xauPln: XauPln) {
    Box(Modifier.fillMaxSize()) {
        Column {
            GoldResults(
                list = state.goldItems,
                xauPln = xauPln,
                model = homeViewModel,
                title = state.title,
                forceFocus = state.forceFocus
            )
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
private fun GoldResults(
    list: List<GoldItem>,
    xauPln: XauPln,
    model: HomeViewModel,
    title: String = stringResource(R.string.gold),
    forceFocus: Boolean
) {
    val context = AmbientContext.current
    LazyColumn {
        item {
            TopBar(title, forceFocus)
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
private fun TopBar(title: String, forceFocus: Boolean = false) {
    val homeViewModel = viewModel<HomeViewModel>()
    val text = remember { mutableStateOf(TextFieldValue()) }
    val textFieldFocusState = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            ResultsSearchView(
                textFieldValue = text.value,
                onTextChanged = { text.value = it },
                // Only show the keyboard if there's no input selector and text field has focus
                keyboardShown = textFieldFocusState.value,
                // Close extended selector if text field receives focus
                onTextFieldFocused = { focused ->
                    textFieldFocusState.value = focused
                },
                focusState = textFieldFocusState.value,
                placeholderText = title,
                backgroundColor = MaterialTheme.colors.background,
                searchAction = { performSearch(homeViewModel, text) },
                forceFocus = forceFocus)
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
) {
    if (viewModel.state.value is HomeViewModel.State.ShowResults) {
        // ensure you can call this
        viewModel.updateSearchKeyword(text.value.text)
    }
    viewModel.showResults()
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
    if (appliedFilters != null && appliedFilters.anyFilterApplied) {
        ScrollableRow(contentPadding = PaddingValues(top = dp8, start = dp16, bottom = dp16)) {
            AnimatedVisibility(visible = appliedFilters.isSortingApplied) {
                Chip(
                    text = stringResource(appliedFilters.sortingType.sortingName),
                    closeable = true,
                    modifier = Modifier.padding(end = dp8),
                    onClick = { viewModel.clearSortingType() }
                )
            }
            appliedFilters.searchPhrase?.let {
                Chip(
                    text = it,
                    closeable = true,
                    modifier = Modifier.padding(end = dp8),
                    onClick = { viewModel.clearSortingType() }
                )
            }
            AnimatedVisibility(visible = appliedFilters.isGoldTypeApplied) {
                Chip(
                    text = stringResource(appliedFilters.goldTypeFilter.typeName),
                    closeable = true,
                    modifier = Modifier.padding(end = dp8),
                    onClick = { viewModel.clearGoldTypeFiltering() }
                )
            }
            AnimatedVisibility(visible = appliedFilters.isCoinTypeApplied) {
                if (appliedFilters.goldTypeFilter != GoldType.BAR) {
                    Chip(
                        text = stringResource(appliedFilters.coinTypeFilter.coinName),
                        closeable = true,
                        modifier = Modifier.padding(end = dp8),
                        onClick = { viewModel.clearCoinTypeFiltering() }
                    )
                }
            }
            AnimatedVisibility(visible = appliedFilters.isWeightTypeApplied) {
                when (val range = appliedFilters.weightRangeFilter) {
                    is CustomWeightRange -> {
                        Chip(
                            text = stringResource(
                                R.string.weight_from_to_chip,
                                range.weightFrom,
                                range.weightTo
                            ),
                            closeable = true,
                            modifier = Modifier.padding(end = dp8),
                            onClick = { viewModel.clearWeightFiltering() }
                        )
                    }
                    is PredefinedWeightRange -> {
                        Chip(
                            text = stringResource(
                                range.predefinedWeightRanges.labelRangeName
                            ),
                            closeable = true,
                            modifier = Modifier.padding(end = dp8),
                            onClick = { viewModel.clearWeightFiltering() }
                        )
                    }
                }
            }
            AnimatedVisibility(visible = appliedFilters.isMintTypeApplied) {
                Chip(
                    text = appliedFilters.mint.fullName,
                    closeable = true,
                    modifier = Modifier.padding(end = dp8),
                    onClick = { viewModel.clearMintFiltering() }
                )
            }
            if (appliedFilters.bothPricesApplied) {
                AnimatedVisibility(visible = appliedFilters.bothPricesApplied) {
                    Chip(
                        text = stringResource(
                            R.string.price_from_to_chip,
                            appliedFilters.priceFromFilter.toFloat(),
                            appliedFilters.priceToFilter.toFloat()
                        ),
                        closeable = true,
                        modifier = Modifier.padding(end = dp8),
                        onClick = {
                            viewModel.clearPriceFromFiltering()
                            viewModel.clearPriceToFiltering()
                        }
                    )
                }
            } else {
                AnimatedVisibility(visible = appliedFilters.isPriceFromApplied) {
                    Chip(
                        text = stringResource(R.string.price_from_chip, appliedFilters.priceFromFilter.toFloat()),
                        closeable = true,
                        modifier = Modifier.padding(end = dp8),
                        onClick = { viewModel.clearPriceFromFiltering() }
                    )
                }
                AnimatedVisibility(visible = appliedFilters.isPriceToApplied) {
                    Chip(
                        text = stringResource(R.string.price_to_chip, appliedFilters.priceToFilter.toFloat()),
                        closeable = true,
                        modifier = Modifier.padding(end = dp8),
                        onClick = { viewModel.clearPriceToFiltering() }
                    )
                }
            }
            AnimatedVisibility(visible = !appliedFilters.showGoldSets) {
                Chip(
                    text = stringResource(R.string.hide_sets),
                    closeable = true,
                    modifier = Modifier.padding(end = dp8),
                    onClick = { viewModel.clearDisplayingGoldSets() }
                )
            }
        }
    }
}
