package com.selfformat.goldpare.androidApp.compose.filtering

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.enums.GoldCoinType
import com.selfformat.goldpare.androidApp.compose.enums.GoldType
import com.selfformat.goldpare.androidApp.compose.enums.Mint
import com.selfformat.goldpare.androidApp.compose.enums.SortingType
import com.selfformat.goldpare.androidApp.compose.enums.WeightRange
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.theme.buttonFontSize
import com.selfformat.goldpare.androidApp.compose.theme.buttonHeight
import com.selfformat.goldpare.androidApp.compose.theme.dp12
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.roundCorner10dp
import com.selfformat.goldpare.androidApp.compose.util.NO_PRICE_FILTERING

@ExperimentalFoundationApi
@Composable
fun FilteringView(homeViewModel: HomeViewModel) {
    val appliedFilters: HomeViewModel.Filters? = homeViewModel.appliedFilters.observeAsState().value

    Box {
        Column {
            TopBar()
            SortingMenu(appliedFilters)
            FilteringGoldTypeMenu(appliedFilters)
            FilteringCoinTypeMenu(appliedFilters)
            FilteringMintMenu(appliedFilters)
            FilterGoldSetsSwitch(appliedFilters)
            PriceFiltering(homeViewModel, appliedFilters)
            FilteringWeightMenu(appliedFilters)
        }
        Row(Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            TextButton(
                modifier = Modifier.fillMaxWidth().height(buttonHeight).padding(
                    start = dp12,
                    end = dp12,
                    top = dp8,
                    bottom = dp8
                ),
                shape = RoundedCornerShape(roundCorner10dp),
                onClick = { homeViewModel.showResults() },
                colors = ButtonDefaults.textButtonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
            ) {
                Text(text = stringResource(R.string.show_results), fontSize = buttonFontSize)
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
        backgroundColor = Color.White,
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

    DropdownMenu(
        toggle = {
            Text(
                text = SortingType.values()[selectedIndex.value].sortingName,
                modifier = Modifier.fillMaxWidth().clickable(
                    onClick = { showMenu.value = true }
                )
            )
        },
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false },
        toggleModifier = Modifier.fillMaxWidth(),
        dropdownModifier = Modifier.fillMaxWidth(),
    ) {
        SortingType.values().forEachIndexed { index, value ->
            DropdownMenuItem(
                onClick = {
                    selectedIndex.value = index
                    showMenu.value = false
                    viewModel.updateSortingType(value)
                }
            ) {
                Text(text = value.sortingName)
            }
        }
    }
}

@Composable
private fun FilteringCoinTypeMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentFilter = appliedFilters?.coinTypeFilter
    val baseIndex = GoldCoinType.values().indexOf(currentFilter)
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(baseIndex) }

    DropdownMenu(
        toggle = {
            Text(
                text = GoldCoinType.values()[selectedIndex.value].coinName,
                modifier = Modifier.fillMaxWidth().clickable(
                    onClick = { showMenu.value = true }
                )
            )
        },
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false },
        toggleModifier = Modifier.fillMaxWidth(),
        dropdownModifier = Modifier.fillMaxWidth()
    ) {
        GoldCoinType.values().forEachIndexed { index, value ->
            DropdownMenuItem(
                onClick = {
                    selectedIndex.value = index
                    showMenu.value = false
                    viewModel.updateCoinTypeFiltering(value)
                }
            ) {
                Text(text = value.coinName)
            }
        }
    }
}

@Composable
private fun FilteringWeightMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentFilter = appliedFilters?.weightFilter
    val baseIndex = WeightRange.values().indexOf(currentFilter)
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(baseIndex) }

    DropdownMenu(
        toggle = {
            Text(
                text = WeightRange.values()[selectedIndex.value].rangeName,
                modifier = Modifier.fillMaxWidth().clickable(
                    onClick = { showMenu.value = true }
                )
            )
        },
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false },
        toggleModifier = Modifier.fillMaxWidth(),
        dropdownModifier = Modifier.fillMaxWidth()
    ) {
        WeightRange.values().forEachIndexed { index, value ->
            DropdownMenuItem(
                onClick = {
                    selectedIndex.value = index
                    showMenu.value = false
                    viewModel.updateWeightFiltering(value)
                }
            ) {
                Text(text = value.rangeName)
            }
        }
    }
}

@Composable
private fun FilterGoldSetsSwitch(appliedFilters: HomeViewModel.Filters?) {
    val currentFilter = appliedFilters?.showGoldSets ?: true
    val showSets = remember { mutableStateOf(currentFilter) }
    val viewModel: HomeViewModel = viewModel()
    Row {
        Text(stringResource(R.string.show_sets))
        Switch(
            checked = showSets.value,
            onCheckedChange = {
                showSets.value = !showSets.value
                viewModel.updateDisplayingGoldSets(showSets.value)
            }
        )
    }
}

@Composable
private fun FilteringMintMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentFilter = appliedFilters?.mint
    val baseIndex = Mint.values().indexOf(currentFilter)
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(baseIndex) }

    DropdownMenu(
        toggle = {
            Text(
                text = Mint.values()[selectedIndex.value].fullName,
                modifier = Modifier.fillMaxWidth().clickable(
                    onClick = { showMenu.value = true }
                )
            )
        },
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false },
        toggleModifier = Modifier.fillMaxWidth(),
        dropdownModifier = Modifier.fillMaxWidth()
    ) {
        Mint.values().forEachIndexed { index, value ->
            DropdownMenuItem(
                onClick = {
                    selectedIndex.value = index
                    showMenu.value = false
                    viewModel.updateMintFiltering(value)
                }
            ) {
                Text(text = value.fullName)
            }
        }
    }
}

@Composable
private fun FilteringGoldTypeMenu(appliedFilters: HomeViewModel.Filters?) {
    val viewModel: HomeViewModel = viewModel()
    val currentFilter = appliedFilters?.goldTypeFilter
    val baseIndex = GoldType.values().indexOf(currentFilter)
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(baseIndex) }

    DropdownMenu(
        toggle = {
            Text(
                text = GoldType.values()[selectedIndex.value].typeName,
                modifier = Modifier.fillMaxWidth().clickable(
                    onClick = { showMenu.value = true }
                )
            )
        },
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false },
        toggleModifier = Modifier.fillMaxWidth(),
        dropdownModifier = Modifier.fillMaxWidth()
    ) {
        GoldType.values().forEachIndexed { index, value ->
            DropdownMenuItem(
                onClick = {
                    selectedIndex.value = index
                    showMenu.value = false
                    viewModel.updateGoldTypeFiltering(value)
                }
            ) {
                Text(text = value.typeName)
            }
        }
    }
}

@Composable
private fun PriceFiltering(viewModel: HomeViewModel, appliedFilters: HomeViewModel.Filters?) {
    val currentPriceFromFilter = appliedFilters?.priceFromFilter
    val currentPriceToFilter = appliedFilters?.priceToFilter
    val textFrom = if (currentPriceFromFilter == NO_PRICE_FILTERING) {
        stringResource(R.string.price_from)
    } else "$currentPriceFromFilter"
    val textTo = if (currentPriceToFilter == NO_PRICE_FILTERING) {
        stringResource(R.string.price_to)
    } else "$currentPriceToFilter"

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(stringResource(R.string.prices))
        PriceEditText(textFrom) { priceFrom ->
            viewModel.updatePriceFromFiltering(priceFrom)
        }
        PriceEditText(textTo) { priceTo ->
            viewModel.updatePriceToFiltering(priceTo)
        }
    }
}

@Composable
private fun PriceEditText(labelText: String, function: (Double) -> Unit) {
    val text = remember { mutableStateOf(TextFieldValue()) }

    OutlinedTextField(
        // TODO(force input type to be digits - for now only keyboard is forced)
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
            if (it.text.isNotEmpty()) {
                function(it.text.toDouble())
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
