package com.selfformat.goldpare.androidApp.compose.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.viewModel
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel

@ExperimentalFoundationApi
@Composable
fun SearchView(
    viewModel: HomeViewModel,
    function: (String) -> Unit,
    placeholderText: String
) {
    val text = remember { mutableStateOf(TextFieldValue()) }
    // TODO(force input type to be digits - for now only keyboard is forced)

    val textFieldFocusState = remember { mutableStateOf(false) }

    CustomSearchView(
        viewModel = viewModel,
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
    viewModel: HomeViewModel,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean,
    placeholderText: String
) {
    Row(
        modifier = Modifier.clickable(onClick = { viewModel.search() }),
        horizontalArrangement = Arrangement.Center
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .preferredHeight(searchHeight)
                    .background(
                        searchBackground,
                        shape = CircleShape
                    ),
            ) {
                Icon(
                    Icons.Filled.Search,
                    Modifier.align(Alignment.CenterStart).padding(start = dp6)
                )
                val lastFocusState = remember { mutableStateOf(FocusState.Inactive) }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { onTextChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dp32, end = dp8)
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
                            .padding(start = dp32, end = dp8),
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
    val viewModel: HomeViewModel = viewModel()
    SearchView(viewModel = viewModel, function = {}, placeholderText = "Szukaj frazy")
}
