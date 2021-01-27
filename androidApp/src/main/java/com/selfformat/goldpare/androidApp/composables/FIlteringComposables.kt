package com.selfformat.goldpare.androidApp.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.data.CustomWeightRange
import com.selfformat.goldpare.androidApp.data.GoldCoinType
import com.selfformat.goldpare.androidApp.data.GoldType
import com.selfformat.goldpare.androidApp.data.Mint
import com.selfformat.goldpare.androidApp.data.PredefinedWeightRange
import com.selfformat.goldpare.androidApp.data.PredefinedWeightRanges
import com.selfformat.goldpare.androidApp.data.SortingType
import com.selfformat.goldpare.androidApp.theme.buttonFontSize
import com.selfformat.goldpare.androidApp.theme.buttonHeight
import com.selfformat.goldpare.androidApp.theme.dividerColor
import com.selfformat.goldpare.androidApp.theme.dividerThickness
import com.selfformat.goldpare.androidApp.theme.dp12
import com.selfformat.goldpare.androidApp.theme.dp16
import com.selfformat.goldpare.androidApp.theme.dp4
import com.selfformat.goldpare.androidApp.theme.dp8
import com.selfformat.goldpare.androidApp.theme.filteringLabelFontSize
import com.selfformat.goldpare.androidApp.theme.fontWeight500
import com.selfformat.goldpare.androidApp.theme.shapes
import com.selfformat.goldpare.androidApp.utils.NO_PRICE_FILTERING
import com.selfformat.goldpare.androidApp.viewModels.HomeViewModel

@ExperimentalLayout
@ExperimentalFoundationApi
@Composable
fun FilteringView(homeViewModel: HomeViewModel) {
    val appliedFilters: HomeViewModel.Filters? = homeViewModel.appliedFilters.observeAsState().value

    Box {
        ScrollableColumn {
            TopBar()
            ProductsFiltering(homeViewModel, appliedFilters)
            CollapsableSection(sectionName = stringResource(R.string.sorting)) { SortingMenu(appliedFilters) }
            WeightSection(homeViewModel, appliedFilters)
            ShowSetsSection(appliedFilters)
            PriceSection(homeViewModel, appliedFilters)
            CollapsableSection(
                collapsed = false,
                sectionName = stringResource(R.string.coin_type)
            ) { FilteringCoinTypeMenu(appliedFilters) }
            CollapsableSection(
                collapsed = false,
                sectionName = stringResource(R.string.mints)
            ) { FilteringMintMenu(appliedFilters) }
            Spacer(modifier = Modifier.preferredHeight(buttonHeight))
        }
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            TextButton(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .clip(shapes.medium)
                    .padding(
                        start = dp12,
                        end = dp12,
                        top = dp8,
                        bottom = dp8
                ),
                onClick = { homeViewModel.showResults() },
                colors = ButtonDefaults.textButtonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
            ) {
                Text(text = stringResource(R.string.show_results), fontSize = buttonFontSize)
            }
        }
    }
}

@Composable
private fun PriceSection(
    homeViewModel: HomeViewModel,
    appliedFilters: HomeViewModel.Filters?
) {
    FilteringLabel(text = stringResource(R.string.prices), modifier = Modifier.padding(dp16))
    PriceFiltering(homeViewModel, appliedFilters)
    FilteringSectionsDivider()
}

@Composable
private fun WeightSection(homeViewModel: HomeViewModel, appliedFilters: HomeViewModel.Filters?) {
    FilteringLabel(text = stringResource(R.string.weights), modifier = Modifier.padding(dp16))
    FilteringWeightMenu(appliedFilters)
    WeightFiltering(homeViewModel, appliedFilters)
    FilteringSectionsDivider()
}

@Composable
private fun ShowSetsSection(appliedFilters: HomeViewModel.Filters?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = dp16, top = dp16, end = dp16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilteringLabel(
            text = stringResource(R.string.show_sets),
            modifier = Modifier.padding()
        )
        FilterGoldSetsSwitch(appliedFilters)
    }
    FilteringSectionsDivider()
}

@Composable
private fun FilterGoldSetsSwitch(appliedFilters: HomeViewModel.Filters?) {
    val currentFilter = appliedFilters?.showGoldSets ?: true
    val showSets = remember { mutableStateOf(currentFilter) }
    val viewModel: HomeViewModel = viewModel()
    Switch(
        checked = showSets.value,
        onCheckedChange = {
            showSets.value = !showSets.value
            viewModel.updateDisplayingGoldSets(showSets.value)
        }
    )
}

@Composable
private fun CollapsableSection(collapsed: Boolean = false, sectionName: String, composable: @Composable (() -> Unit)) {
    val isCollapsed = remember { mutableStateOf(collapsed) }
    val padding = if (isCollapsed.value) PaddingValues(top = dp16, end = dp16, start = dp16) else PaddingValues(dp16)

    CollapsableFilteringLabel(
        text = sectionName,
        modifier = Modifier.padding(padding),
        collapsed = isCollapsed.value,
        onClick = {
            isCollapsed.value = !isCollapsed.value
        }
    )
    if (!isCollapsed.value) {
        composable()
    }
    FilteringSectionsDivider()
}

@Composable
fun ProductsFiltering(homeViewModel: HomeViewModel, appliedFilters: HomeViewModel.Filters?) {
    Column {
        FilteringLabel(text = stringResource(R.string.products), modifier = Modifier.padding(dp16))
        ProductsChips(appliedFilters, homeViewModel)
        FilteringSectionsDivider()
    }
}

@Composable
fun ProductsChips(appliedFilters: HomeViewModel.Filters?, homeViewModel: HomeViewModel) {
    val currentFilter = appliedFilters?.goldTypeFilter
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(modifier = Modifier.preferredWidth(dp16))
        Chip(
            text = stringResource(R.string.all),
            selected = currentFilter == GoldType.ALL,
            closeable = false,
            modifier = Modifier.weight(1f),
            onClick = { homeViewModel.updateGoldTypeFiltering(GoldType.ALL) }
        )
        Spacer(modifier = Modifier.preferredWidth(dp8))
        Chip(
            text = stringResource(R.string.coins),
            leadingIcon = vectorResource(R.drawable.ic_coin_stack),
            selected = currentFilter == GoldType.COIN,
            closeable = false,
            modifier = Modifier.weight(1f),
            onClick = { homeViewModel.updateGoldTypeFiltering(GoldType.COIN) }
        )
        Spacer(modifier = Modifier.preferredWidth(dp8))
        Chip(
            text = stringResource(R.string.bars),
            leadingIcon = vectorResource(R.drawable.ic_gold_bar),
            selected = currentFilter == GoldType.BAR,
            closeable = false,
            modifier = Modifier.weight(1f),
            onClick = { homeViewModel.updateGoldTypeFiltering(GoldType.BAR) }
        )
        Spacer(modifier = Modifier.preferredWidth(dp16))
    }
}

@Composable
private fun FilteringLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6.copy(fontSize = filteringLabelFontSize, fontWeight = fontWeight500),
        modifier = modifier
    )
}

@Composable
private fun CollapsableFilteringLabel(
    text: String,
    modifier: Modifier = Modifier,
    collapsed: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilteringLabel(text, modifier)
        IconToggleButton(
            checked = collapsed,
            onCheckedChange = { onClick() },
            modifier = modifier.padding(end = dp16)
        ) {
            if (collapsed) {
                Icon(imageVector = Icons.Filled.ArrowDropDown)
            } else {
                Icon(imageVector = Icons.Filled.ArrowDropUp)
            }
        }
    }
}

@Composable
private fun TopBar() {
    val homeViewModel = viewModel<HomeViewModel>()
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.filtering), modifier = Modifier.padding(start = 0.dp))
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
        elevation = 1.dp,
        actions = {
            Text(
                text = stringResource(R.string.clear_filters),
                modifier = Modifier.padding(end = dp12).clickable(onClick = {
                    homeViewModel.clearFilters()
                })
            )
        }
    )
}

@Composable
private fun SortingMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentSorting = appliedFilters?.sortingType
    val baseIndex = SortingType.values().indexOf(currentSorting)
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(baseIndex) }

    SortingType.values().forEachIndexed { index, value ->
        Chip(
            text = stringResource(value.sortingName),
            onClick = {
                selectedIndex.value = index
                showMenu.value = false
                viewModel.updateSortingType(value)
            },
            selected = currentSorting == value,
            closeable = false,
            modifier = Modifier.padding(start = dp16, bottom = dp8)
        )
    }
}

@ExperimentalLayout
@Composable
private fun FilteringCoinTypeMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentFilter = appliedFilters?.coinTypeFilter
    val baseIndex = GoldCoinType.values().indexOf(currentFilter)
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(baseIndex) }

    Row(Modifier.padding(start = dp16)) {
        FlowRow(
            mainAxisSpacing = dp8,
            crossAxisSpacing = dp4
        ) {
            GoldCoinType.values().forEachIndexed { index, value ->
                Chip(
                    text = stringResource(value.coinName),
                    onClick = {
                        selectedIndex.value = index
                        showMenu.value = false
                        viewModel.updateCoinTypeFiltering(value)
                    },
                    selected = currentFilter == value,
                    closeable = false,
                )
            }
        }
    }
}

@Composable
private fun FilteringWeightMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentFilter = appliedFilters?.weightRangeFilter
    val isCustom = currentFilter != null && currentFilter is CustomWeightRange
    if (!isCustom) {
        val baseIndex = PredefinedWeightRanges.values().indexOf(currentFilter)
        val showMenu = remember { mutableStateOf(false) }
        val selectedIndex = remember { mutableStateOf(baseIndex) }

        ScrollableRow(contentPadding = PaddingValues(start = dp16, end = dp16)) {
            PredefinedWeightRanges.values().forEachIndexed { index, value ->
                Chip(
                    text = stringResource(value.rangeName),
                    onClick = {
                        selectedIndex.value = index
                        showMenu.value = false
                        viewModel.updateWeightFiltering(PredefinedWeightRange(value))
                    },
                    selected = currentFilter == PredefinedWeightRange(value),
                    closeable = false,
                    modifier = Modifier.padding(end = dp8)
                )
            }
        }
    }
}

@ExperimentalLayout
@Composable
private fun FilteringMintMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentFilter = appliedFilters?.mint
    val baseIndex = Mint.values().indexOf(currentFilter)
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(baseIndex) }

    Row(Modifier.padding(start = dp16)) {
        FlowRow(
            mainAxisSpacing = dp8,
            crossAxisSpacing = dp4
        ) {
            Mint.values().forEachIndexed { index, value ->
                Chip(
                    text = value.fullName,
                    onClick = {
                        selectedIndex.value = index
                        showMenu.value = false
                        viewModel.updateMintFiltering(value)
                    },
                    selected = currentFilter == value,
                    closeable = false,
                )
            }
        }
    }
}

@Composable
private fun PriceFiltering(viewModel: HomeViewModel, appliedFilters: HomeViewModel.Filters?) {
    val currentPriceFromFilter = appliedFilters?.priceFromFilter
    val currentPriceToFilter = appliedFilters?.priceToFilter
    val textFrom = stringResource(R.string.from)
    val textTo = stringResource(R.string.to)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = dp16, end = dp16)
    ) {
        FiltersEditText(
            labelText = textFrom,
            initialValue = if (currentPriceFromFilter == NO_PRICE_FILTERING) "" else currentPriceFromFilter.toString(),
            modifier = Modifier.weight(1f)
        ) { priceFrom ->
            viewModel.updatePriceFromFiltering(priceFrom)
        }
        Text(text = "-", modifier = Modifier.padding(dp4))
        FiltersEditText(
            labelText = textTo,
            initialValue = if (currentPriceToFilter == NO_PRICE_FILTERING) "" else currentPriceToFilter.toString(),
            modifier = Modifier.weight(1f)
        ) { priceTo ->
            viewModel.updatePriceToFiltering(priceTo)
        }
        Text(text = "PLN", modifier = Modifier.padding(dp4))
    }
}

@Composable
private fun WeightFiltering(viewModel: HomeViewModel, appliedFilters: HomeViewModel.Filters?) {
    val currentFilter = appliedFilters?.weightRangeFilter
    val isCustom = currentFilter != null && currentFilter is CustomWeightRange
    val currentWeightFromFilter = if (isCustom) appliedFilters?.weightRangeFilter?.weightFrom else null
    val currentWeightToFilter = if (isCustom) appliedFilters?.weightRangeFilter?.weightTo else null
    val textFrom = stringResource(R.string.from)
    val textTo = stringResource(R.string.to)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = dp16, end = dp16)
    ) {
        FiltersEditText(
            labelText = textFrom,
            initialValue = currentWeightFromFilter?.toString() ?: "",
            modifier = Modifier.weight(1f)
        ) { weightFrom ->
            viewModel.updateCustomWeightFromFiltering(weightFrom)
        }
        Text(text = "-", modifier = Modifier.padding(dp4))
        FiltersEditText(
            labelText = textTo,
            initialValue = currentWeightToFilter?.toString() ?: "",
            modifier = Modifier.weight(1f)
        ) { weightTo ->
            viewModel.updateCustomWeightToFiltering(weightTo)
        }
        Text(text = stringResource(R.string.grams), modifier = Modifier.padding(dp4))
    }
}

@Composable
private fun FiltersEditText(
    labelText: String,
    initialValue: String = "",
    modifier: Modifier,
    function: (Double) -> Unit
) {
    val text = remember { mutableStateOf(TextFieldValue(text = initialValue)) }
    val isInputEmpty = text.value.text.isEmpty()
    val errorState = if (!isInputEmpty) text.value.text.toDoubleOrNull() == null else false

    OutlinedTextField(
        modifier = modifier,
        placeholder = {
            Text(labelText)
        },
        label = {
            Text(labelText)
        },
        value = text.value,
        singleLine = true,
        onValueChange = {
            text.value = it
        },
        isErrorValue = errorState,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        onImeActionPerformed = { action, softwareController ->
            if (action == ImeAction.Done) {
                softwareController?.hideSoftwareKeyboard()
                if (action == ImeAction.Done) {
                    if (!errorState && !isInputEmpty) function(text.value.text.toDouble())
                }
            }
        }
    )
}

@Composable
fun FilteringSectionsDivider() {
    Divider(
        color = dividerColor,
        thickness = dividerThickness,
        modifier = Modifier.padding(top = dp16, start = dp16, end = dp16)
    )
}
