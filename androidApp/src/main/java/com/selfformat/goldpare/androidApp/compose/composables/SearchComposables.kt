package com.selfformat.goldpare.androidApp.compose.composables

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
import androidx.compose.runtime.onActive
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.theme.dp4
import com.selfformat.goldpare.androidApp.compose.theme.dp6
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.searchHeight

@ExperimentalFoundationApi
@Composable
fun HomeFakeSearchView(
    function: () -> Unit,
) {
    Row(
        Modifier
            .background(MaterialTheme.colors.primaryVariant, shape = CircleShape)
            .clip(CircleShape)
            .clickable(onClick = { function() })
            .fillMaxWidth()
            .preferredHeight(searchHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Search,
            Modifier.padding(start = dp6)
        )
        val disableContentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        Text(
            modifier = Modifier
                .padding(start = dp4, end = dp8),
            text = stringResource(R.string.search),
            style = MaterialTheme.typography.body1.copy(color = disableContentColor)
        )
    }
}

@SuppressWarnings("LongParameterList", "LongMethod")
@ExperimentalFoundationApi
@Composable
fun ResultsSearchView(
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean,
    placeholderText: String,
    backgroundColor: Color,
    keyboardShown: Boolean,
    searchAction: () -> Unit,
    forceFocus: Boolean
) {
    // Grab a reference to the keyboard controller whenever text input starts
    val keyboardController = remember { mutableStateOf<SoftwareKeyboardController?>(null) }

    // Show or hide the keyboard
    onCommit(keyboardController, keyboardShown) { // Guard side-effects against failed commits
        keyboardController.let {
            if (it.value != null) {
                if (keyboardShown) it.value?.showSoftwareKeyboard() else it.value?.hideSoftwareKeyboard()
            }
        }
    }

    val lastFocusState = remember { mutableStateOf(FocusState.Inactive) }
    val focusRequester = FocusRequester()

    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        val focusRequesterModifier = if (forceFocus) Modifier.focusRequester(focusRequester) else Modifier
        Surface {
            Box(
                modifier = Modifier
                    .preferredHeight(searchHeight)
                    .background(
                        backgroundColor,
                        shape = CircleShape
                    ),
            ) {
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { onTextChanged(it) },
                    modifier = focusRequesterModifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = dp8)
                        .align(Alignment.CenterStart)
                        .onFocusChanged { state ->
                            if (lastFocusState.value != state) {
                                onTextFieldFocused(state == FocusState.Active)
                            }
                            lastFocusState.value = state
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    onTextInputStarted = { keyboardController.value = it },
                    onImeActionPerformed = {
                        keyboardController.value?.hideSoftwareKeyboard()
                        searchAction()
                    },
                    maxLines = 1,
                    singleLine = true,
                    cursorColor = AmbientContentColor.current,
                    textStyle = AmbientTextStyle.current.copy(color = AmbientContentColor.current)
                )
                val iconModifier = if (textFieldValue.text.isNotEmpty()) {
                    Modifier.clickable(onClick = searchAction)
                } else Modifier
                Icon(
                    Icons.Filled.Search,
                    iconModifier.align(Alignment.CenterEnd).padding(end = dp6)
                )
                if (textFieldValue.text.isEmpty() && !focusState) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 0.dp, end = dp8),
                        text = placeholderText,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
    if (forceFocus) {
        onActive {
            focusRequester.requestFocus()
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun HomeFakeSearchPreview() {
    HomeFakeSearchView(function = {})
}

@ExperimentalFoundationApi
@Preview
@Composable
fun ResultSearchPreview() {
    ResultsSearchView(
        textFieldValue = TextFieldValue(text = "hello"),
        onTextChanged = { },
        // Only show the keyboard if there's no input selector and text field has focus
        keyboardShown = false,
        // Close extended selector if text field receives focus
        onTextFieldFocused = { },
        focusState = false,
        placeholderText = "Placeholder text",
        backgroundColor = MaterialTheme.colors.background,
        searchAction = { },
        forceFocus = false
    )
}
