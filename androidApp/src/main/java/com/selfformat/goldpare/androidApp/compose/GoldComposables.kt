package com.selfformat.goldpare.androidApp.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.ui.gray200
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.*

@ExperimentalFoundationApi
@Composable
fun HomeView() {
    Box(Modifier.fillMaxSize()) {
        val viewModel: HomeViewModel = viewModel()
        viewModel.loadGoldItems()
        val state = viewModel.state.observeAsState().value
        viewModel.loadXauPln()
        val xauPln: XauPln? = viewModel.xaupln.observeAsState().value
        xauPln.let { xau_to_pln ->
            if (xauPln != null) {
                state.let {
                    when (it) {
                        is HomeViewModel.State.Loaded -> {
                            Column {
                                TopSection(xau_to_pln)
                                SortingMenu()
                                FilteringCoinTypeMenu()
                                FilteringGoldTypeMenu()
                                FilteringMintMenu()
                                FilterGoldSetsSwitch()
                                PriceFiltering(viewModel)
                                FilteringWeightMenu()
                                FilterableLazyRow(list = it.goldItems, xauPln = xau_to_pln!!)
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
        }
        Row(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    brush = verticalGradient(
                        0.0f to Color.Transparent,
                        1.0f to Color.White
                    )

                )
        ) {

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
fun PriceEditText(labelText: String, function: (Double) -> Unit) {
    val text = remember { mutableStateOf(TextFieldValue()) }

    OutlinedTextField(
        // TODO: force input type to be digits (for now only keyboard is forced)
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

@ExperimentalFoundationApi
@Composable
private fun TopSection(xau_to_pln: XauPln?) {
    val viewModel: HomeViewModel = viewModel()
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(60.dp).padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
    ) {
        Image(vectorResource(id = R.drawable.ic_temp_mini_logo), modifier = Modifier.padding(end = 16.dp))
        val formattedXauPln = "%.2f".format(xau_to_pln!!.price)
        Column(
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text("Kurs złota", fontWeight = FontWeight.Bold, style = TextStyle.Default.copy(fontSize = 12.sp))
            Text("$formattedXauPln zł/oz", style = TextStyle.Default.copy(fontSize = 12.sp))
        }
        SearchView(
            modifier = Modifier
                .preferredHeight(48.dp)
                .fillMaxWidth(),
            function = { searchedPhrase ->
                viewModel.updateSearchKeyword(searchedPhrase)
            },
            placeholderText = "Szukaj"
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun SearchView(
    function: (String) -> Unit,
    modifier: Modifier,
    placeholderText: String
) {
    val text = remember { mutableStateOf(TextFieldValue()) }
    // TODO: force input type to be digits (for now only keyboard is forced)

    val textFieldFocusState = remember { mutableStateOf(false) }

    CustomSearchView(
        modifier = modifier,
        textFieldValue = text.value,
        onTextFieldFocused = { focused ->
            textFieldFocusState.value = focused
        },
        focusState = textFieldFocusState.value,
        onTextChanged = {
            text.value = it
            function(it.text)
        },
        placeholderText = placeholderText
    )
}

@ExperimentalFoundationApi
@Composable
private fun CustomSearchView(
    modifier: Modifier,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean,
    placeholderText: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .preferredHeight(48.dp)
                    .background(
                        Color(0xFFEEEEEE),
                        shape = CircleShape
                ),
            ) {
                Icon(
                    Icons.Filled.Search,
                    Modifier.align(Alignment.CenterStart).padding(start = 6.dp)
                )
                val lastFocusState = remember { mutableStateOf(FocusState.Inactive) }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { onTextChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 8.dp)
                        .align(Alignment.CenterStart)
                        .onFocusChanged { state ->
                            if (lastFocusState.value != state) {
                                onTextFieldFocused(state == FocusState.Active)
                            }
                            lastFocusState.value = state
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    onTextInputStarted = { },
                    maxLines = 1,
                    singleLine = true,
                    cursorColor = AmbientContentColor.current,
                    textStyle = AmbientTextStyle.current.copy(color = AmbientContentColor.current)
                )

                val disableContentColor =
                    MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                if (textFieldValue.text.isEmpty() && !focusState) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 32.dp, end = 8.dp),
                        text = placeholderText,
                        style = MaterialTheme.typography.body1.copy(color = disableContentColor)
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun SearchPreview() {
    SearchView(function = {}, modifier = Modifier, placeholderText = "Szukaj frazy")
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
private fun FilteringWeightMenu() {
    val viewModel: HomeViewModel = viewModel()
    val showMenu = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

    DropdownMenu(
        toggle = {
            Text(text = WeightRanges.values()[selectedIndex.value].rangeName,
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
        WeightRanges.values().forEachIndexed { index, value ->
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
fun FilterGoldSetsSwitch() {
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
fun FilterableLazyRow(list: List<GoldItem>, xauPln: XauPln) {
    val context = AmbientContext.current
    LazyColumn {
        list.forEach {
            item {
                GoldRow(it, xauPln) {
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
fun GoldRow(item: GoldItem, xauPln: XauPln, onClick: (() -> Unit)) {
    Card(
        elevation = 34.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        val formattedWeightInGrams = "%.2f".format(item.weightInGrams)
        val formattedPriceDouble = "%.2f".format(item.priceDouble)
        val formattedPricePerOunce = "%.2f".format(item.pricePerOunce)
        val formattedPriceMarkup = "%.2f".format(item.priceMarkup(xauPln.price))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.padding(12.dp)) {
                if (item.img_url != null) {
                    GlideSuperImage(
                        item.img_url!!,
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
            Column(Modifier.padding(16.dp)) {
                Text(text = item.title, fontWeight = FontWeight.Bold)
                Divider(color = gray200, thickness = 1.dp)
                Row {
                    Text(
                        text = "$formattedPriceDouble zł",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "($formattedPricePerOunce/oz)",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(vectorResource(id = R.drawable.ic_gram), modifier = Modifier.padding(end = 8.dp))
                    Text(text = "$formattedWeightInGrams g", Modifier.padding(end = 16.dp))
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