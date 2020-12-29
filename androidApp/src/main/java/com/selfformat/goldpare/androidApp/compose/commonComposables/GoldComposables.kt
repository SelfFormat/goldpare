package com.selfformat.goldpare.androidApp.compose.commonComposables

import android.os.Build
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.home.HomeViewModel
import com.selfformat.goldpare.androidApp.compose.theme.SHADOW_ALPHA
import com.selfformat.goldpare.androidApp.compose.theme.dividerColor
import com.selfformat.goldpare.androidApp.compose.theme.dividerThickness
import com.selfformat.goldpare.androidApp.compose.theme.dp12
import com.selfformat.goldpare.androidApp.compose.theme.dp16
import com.selfformat.goldpare.androidApp.compose.theme.dp32
import com.selfformat.goldpare.androidApp.compose.theme.dp6
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.gradientHeight
import com.selfformat.goldpare.androidApp.compose.theme.imageCornersShape
import com.selfformat.goldpare.androidApp.compose.theme.imageWidth
import com.selfformat.goldpare.androidApp.compose.theme.noElevation
import com.selfformat.goldpare.androidApp.compose.theme.shapes
import com.selfformat.goldpare.androidApp.compose.util.drawColoredShadow
import com.selfformat.goldpare.androidApp.compose.util.mintFullName
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem

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

@ExperimentalUnsignedTypes
@SuppressWarnings("LongMethod")
@Composable
fun GoldCard(item: GoldItem, xauPln: XauPln, onClick: (() -> Unit)) {
    val modifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // TODO fix too high api needed for this
        Modifier
            .drawColoredShadow(
                MaterialTheme.colors.onBackground,
                alpha = SHADOW_ALPHA,
                shadowRadius = dp32
            ).padding(
                top = dp6,
                bottom = dp8,
                start = dp16,
                end = dp16
            )
    } else {
        Modifier.padding(
            top = dp6,
            bottom = dp8,
            start = dp16,
            end = dp16
        )
    }

    Card(
        elevation = noElevation,
        modifier = modifier.fillMaxWidth().clip(shapes.medium).clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.padding(dp12)) {
                if (item.image != null) {
                    ImageFromUrl(
                        item.image!!,
                        modifier = Modifier.width(imageWidth).clip(imageCornersShape))
                }
            }
            Column(Modifier.padding(dp16)) {
                Text(text = item.title, fontWeight = FontWeight.Bold)
                Divider(color = dividerColor, thickness = dividerThickness)
                Row {
                    item.priceDouble?.let {
                        Text(
                            text = stringResource(R.string.price_in_zloty, it),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = dp16)
                        )
                    }
                    item.pricePerOunce?.let {
                        Text(
                            text = stringResource(R.string.price_per_oz, it),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(vectorResource(id = R.drawable.ic_gram), modifier = Modifier.padding(end = dp8))
                    item.weightInGrams?.let {
                        Text(text = stringResource(R.string.weight_in_grams, it), Modifier.padding(end = dp16))
                    }
                    item.priceMarkupInPercentage(xauPln.price)?.let {
                        Text(text = stringResource(R.string.price_markup, it))
                    }
                }
                Text(text = stringResource(R.string.mint, mintFullName(item.website, AmbientContext.current)))
                if (item.quantity > 1) {
                    Text(text = stringResource(R.string.items_included, item.quantity))
                }
            }
        }
    }
}
