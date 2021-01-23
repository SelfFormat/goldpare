package com.selfformat.goldpare.androidApp.compose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.theme.CHIP_CLOSE_ICON_SCALE
import com.selfformat.goldpare.androidApp.compose.theme.dp12
import com.selfformat.goldpare.androidApp.compose.theme.dp4
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.gradientHeight
import com.selfformat.goldpare.androidApp.compose.viewModels.HomeViewModel

@Composable
internal fun BottomNavigationBar(homeViewModel: HomeViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        androidx.compose.material.BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background,
            contentColor = Color.Gray
        ) {
            BottomNavigationItem(icon = {
                Icon(Icons.Filled.Home)
            }, selected = true, onClick = { homeViewModel.backToHome() })
            BottomNavigationItem(icon = {
                Icon(Icons.Filled.FilterList)
            }, selected = true, onClick = { homeViewModel.showFiltering() })
            BottomNavigationItem(icon = {
                Icon(Icons.Filled.Notifications)
            }, selected = true, onClick = { homeViewModel.goToBookmarks() })
            BottomNavigationItem(icon = {
                Icon(Icons.Filled.Settings)
            }, selected = true, onClick = { homeViewModel.goToSettings() })
        }
    }
}

@Composable
internal fun BottomGradient() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = Modifier
                .height(gradientHeight)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        1.0f to MaterialTheme.colors.background
                    )
                ),
        ) {
            // This is gradient overlay effect from the bottom of the screen
        }
    }
}

@Composable
fun ErrorView(throwable: Throwable) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = stringResource(R.string.error) + throwable,
        textAlign = TextAlign.Center
    )
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.loading))
    }
}

@Composable
fun Chip(
    text: String,
    closeable: Boolean,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    leadingIcon: ImageVector? = null,
    onClick: (() -> Unit)
) {
    val backgroundColor: Color = if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
    val elementsColor: Color = if (selected) MaterialTheme.colors.background else MaterialTheme.colors.onBackground
    val endTextPadding = if (closeable) dp4 else dp12
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(color = backgroundColor)
            .clickable(onClick = onClick)
            .wrapContentWidth()
            .clipToBounds(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                tint = elementsColor
            )
        }
        Text(
            text = text,
            color = elementsColor,
            modifier = Modifier.padding(end = endTextPadding, start = dp12, top = dp8, bottom = dp8)
        )
        if (closeable) {
            Icon(
                imageVector = Icons.Filled.Close,
                modifier = Modifier.scale(CHIP_CLOSE_ICON_SCALE).padding(end = dp12, top = dp8, bottom = dp8),
                tint = elementsColor
            )
        }
    }
}
