package com.selfformat.goldpare.androidApp.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.compose.ui.GoldpareTheme
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.Mint

@Composable
fun HomeView() {
    val viewModel: HomeViewModel = viewModel()
    viewModel.loadGoldItems()
    val state = viewModel.state.observeAsState().value
    state.let {
        when (it) {
            is HomeViewModel.State.Loaded -> {
                Column {
                    SortingMenu()
                    FilteringCoinTypeMenu()
                    FilteringGoldTypeMenu()
                    FilteringMintMenu()
                    FilterableLazyRow(list = it.goldItems)
                }
            }
            is HomeViewModel.State.Error -> {
                Text(text = "Error: ${it.throwable}")
            }
            is HomeViewModel.State.Loading -> {
                Text(text = "Loading...")
            }
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
            Text(text = SortingType.values()[selectedIndex.value].sortingName,
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
            Text(text = GoldCoinType.values()[selectedIndex.value].coinName,
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
private fun FilteringMintMenu() {
    val viewModel: HomeViewModel = viewModel()
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

    DropdownMenu(
        toggle = {
            Text(text = Mint.values()[selectedIndex.value].fullName,
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
            Text(text = GoldType.values()[selectedIndex.value].typeName,
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
fun FilterableLazyRow(
    list: List<GoldItem>,
) {
    val context = AmbientContext.current
    LazyColumn {
        list.forEach {
            item {
                GoldRow(it) {
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
fun GoldRow(item: GoldItem, onClick: (() -> Unit)) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        val formattedWeightInGrams = "%.2f".format(item.weightInGrams)
        val formattedPricePerOunce = "%.2f".format(item.pricePerOunce)
        val formattedPriceMarkup = "%.2f".format(item.priceMarkup(6863.62)) // TODO: get stock price info from new API call

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                if (item.img_url != null) {
                    GlideSuperImage(
                        item.img_url!!,
                        modifier = Modifier.width(120.dp)
                    )
                }
            }
            Column(
                Modifier
                    .padding(16.dp)
            ) {
                Text(text = item.title, fontWeight = FontWeight.Bold)
                Text(text = "cena produktu: ${item.price.orEmpty()}")
                Text(text = "cena 1 uncji: $formattedPricePerOunce")
                Text(text = "waga w gramach: $formattedWeightInGrams")
                Text(text = "marża: $formattedPriceMarkup%")
                Text(text = "sklep: ${item.mintFullName}")
                if (item.quantity > 1) {
                    Text(text = "sztuk w zestawie: ${item.quantity}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GoldpareTheme {
        GoldRow(item = GoldItem.fakeGoldItem, onClick = { })
    }
}