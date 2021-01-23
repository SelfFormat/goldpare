package com.selfformat.goldpare.androidApp.compose.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintSet
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.theme.SHADOW_ALPHA
import com.selfformat.goldpare.androidApp.compose.theme.dividerColor
import com.selfformat.goldpare.androidApp.compose.theme.dividerThickness
import com.selfformat.goldpare.androidApp.compose.theme.dp12
import com.selfformat.goldpare.androidApp.compose.theme.dp16
import com.selfformat.goldpare.androidApp.compose.theme.dp32
import com.selfformat.goldpare.androidApp.compose.theme.dp4
import com.selfformat.goldpare.androidApp.compose.theme.dp6
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.gray200
import com.selfformat.goldpare.androidApp.compose.theme.imageCornersShape
import com.selfformat.goldpare.androidApp.compose.theme.imageWidth
import com.selfformat.goldpare.androidApp.compose.theme.mediumFontSize
import com.selfformat.goldpare.androidApp.compose.theme.noElevation
import com.selfformat.goldpare.androidApp.compose.theme.primaryGold
import com.selfformat.goldpare.androidApp.compose.theme.shapes
import com.selfformat.goldpare.androidApp.compose.utils.drawColoredShadow
import com.selfformat.goldpare.androidApp.compose.utils.mintFullName
import com.selfformat.goldpare.shared.models.XauPln
import com.selfformat.goldpare.shared.models.GoldItem

@Composable
fun GoldCard(item: GoldItem, xauPln: XauPln, onClick: (() -> Unit)) {
    Card(
        elevation = noElevation,
        modifier = Modifier
            .drawColoredShadow(
                MaterialTheme.colors.onBackground,
                alpha = SHADOW_ALPHA,
                shadowRadius = dp32
            )
            .padding(
                top = dp6,
                bottom = dp8,
                start = dp16,
                end = dp16
            )
            .fillMaxWidth()
            .clip(shapes.medium)
            .clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.padding(dp12)) {
                if (item.image != null) {
                    ImageFromUrl(
                        item.image!!,
                        modifier = Modifier.width(imageWidth).clip(imageCornersShape)
                    )
                }
            }
            Column(Modifier.padding(dp16)) {
                TitleWithBookmarkRow(item)
                Divider(
                    color = dividerColor,
                    thickness = dividerThickness,
                    modifier = Modifier.padding(top = dp8, bottom = dp8)
                )
                PricesRow(item, dp4)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeightView(dp8, item, dp16)
                    PriceMarkupView(item, xauPln)
                }
                MintRow(item)
                ItemsIncludedRow(item, mediumFontSize)
            }
        }
    }
}

@Composable
private fun TitleWithBookmarkRow(item: GoldItem) {
    ConstraintLayout(
        ConstraintSet {
            val text = createRefFor("text")
            val icon = createRefFor("icon")
            constrain(text) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(icon.start)
            }
            constrain(icon) {
                start.linkTo(text.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        }, modifier = Modifier.padding(start = dp16, end = dp16)
    ) {
        Text(
            text = item.title.trim(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.layoutId("text")
        )
        BookmarkButton(isBookmarked = true, modifier = Modifier.layoutId("icon")) { }
    }
}

@Composable
private fun BookmarkButton(isBookmarked: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconToggleButton(checked = isBookmarked, onCheckedChange = { onClick() }, modifier = modifier) {
        if (isBookmarked) {
            Icon(imageVector = Icons.Filled.Star, tint = primaryGold)
        } else {
            Icon(imageVector = Icons.Filled.StarBorder, tint = gray200)
        }
    }
}

@Composable
private fun PricesRow(item: GoldItem, dp4: Dp) {
    Row {
        item.priceDouble?.let {
            Text(
                text = stringResource(R.string.price_in_zloty, it),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = dp4)
            )
        }
        item.pricePerOunce?.let {
            Text(
                text = stringResource(R.string.price_per_oz, it),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun PriceMarkupView(item: GoldItem, xauPln: XauPln) {
    item.priceMarkupInPercentage(xauPln.price)?.let {
        Text(
            text = stringResource(R.string.price_markup, it),
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
private fun WeightView(dp8: Dp, item: GoldItem, dp16: Dp) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(vectorResource(id = R.drawable.ic_gram), modifier = Modifier.padding(end = dp8))
        item.weightInGrams?.let {
            Text(
                text = stringResource(R.string.weight_in_grams, it),
                Modifier.padding(end = dp16),
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun ItemsIncludedRow(item: GoldItem, mediumFontSize: TextUnit) {
    if (item.quantity > 1) {
        Text(
            text = stringResource(R.string.items_included, item.quantity),
            color = MaterialTheme.colors.primary,
            fontSize = mediumFontSize
        )
    }
}

@Composable
private fun MintRow(item: GoldItem) {
    Text(
        text = stringResource(R.string.mint, mintFullName(item.website, AmbientContext.current)),
        color = MaterialTheme.colors.primary,
        fontSize = mediumFontSize,
        modifier = Modifier.padding(top = dp8)
    )
}

@Preview("Bookmark Button")
@Composable
fun BookmarkButtonPreview() {
    Surface {
        BookmarkButton(isBookmarked = false, onClick = { })
    }
}

@Preview("Bookmark Button Bookmarked")
@Composable
fun BookmarkButtonBookmarkedPreview() {
    Surface {
        BookmarkButton(isBookmarked = true, onClick = { })
    }
}
