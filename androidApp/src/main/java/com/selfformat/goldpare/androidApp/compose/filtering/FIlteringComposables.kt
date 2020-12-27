package com.selfformat.goldpare.androidApp.compose.filtering

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.shared.model.GoldCoinType
import com.selfformat.goldpare.shared.model.GoldType
import com.selfformat.goldpare.shared.model.Mint
import com.selfformat.goldpare.shared.model.SortingType
import com.selfformat.goldpare.shared.model.WeightRange

@ExperimentalFoundationApi
@Composable
fun FilteringView(homeViewModel: HomeViewModel) {
    Box(Modifier.fillMaxSize()) {
        Column {
            SortingMenu()
            FilteringCoinTypeMenu()
            FilteringGoldTypeMenu()
            FilteringMintMenu()
            FilterGoldSetsSwitch()
            PriceFiltering(homeViewModel)
            FilteringWeightMenu()
        }
    }
}

@Composable
private fun SortingMenu() {
    val viewModel: HomeViewModel = viewModel()
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

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
        dropdownModifier = Modifier.fillMaxWidth()
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
private fun FilteringCoinTypeMenu() {
    val viewModel: HomeViewModel = viewModel()
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

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
private fun FilteringWeightMenu() {
    val viewModel: HomeViewModel = viewModel()
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

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
private fun FilterGoldSetsSwitch() {
    val showSets = remember { mutableStateOf(true) }
    val viewModel: HomeViewModel = viewModel()
    Row {
        Text("show sets: ")
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
private fun FilteringMintMenu() {
    val viewModel: HomeViewModel = viewModel()
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

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
private fun FilteringGoldTypeMenu() {
    val viewModel: HomeViewModel = viewModel()
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

    DropdownMenu(
        toggle = {
            Text(
                text = GoldType.values()[selectedIndex.value].typeCode,
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
private fun PriceFiltering(viewModel: HomeViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Ceny")
        PriceEditText("OD") { priceFrom ->
            viewModel.updatePriceFromFiltering(priceFrom)
        }
        PriceEditText("DO") { priceTo ->
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
