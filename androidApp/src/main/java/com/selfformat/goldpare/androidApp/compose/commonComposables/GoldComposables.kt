package com.selfformat.goldpare.androidApp.compose.commonComposables

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.theme.SHADOW_ALPHA
import com.selfformat.goldpare.androidApp.compose.theme.cardCorners
import com.selfformat.goldpare.androidApp.compose.theme.dividerColor
import com.selfformat.goldpare.androidApp.compose.theme.dividerThickness
import com.selfformat.goldpare.androidApp.compose.theme.dp12
import com.selfformat.goldpare.androidApp.compose.theme.dp16
import com.selfformat.goldpare.androidApp.compose.theme.dp6
import com.selfformat.goldpare.androidApp.compose.theme.dp8
import com.selfformat.goldpare.androidApp.compose.theme.imageWidth
import com.selfformat.goldpare.androidApp.compose.theme.noElevation
import com.selfformat.goldpare.androidApp.compose.theme.shadowColor
import com.selfformat.goldpare.androidApp.compose.util.drawColoredShadow
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.GoldItem

@Composable
fun ErrorView(throwable: Throwable) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Error: $throwable",
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
        Text("Loading...")
    }
}

@ExperimentalUnsignedTypes
@SuppressWarnings("LongMethod")
@Composable
fun GoldCard(item: GoldItem, xauPln: XauPln, onClick: (() -> Unit)) {
    val modifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Modifier.padding(
                top = dp6,
                bottom = dp8,
                start = dp16,
                end = dp16
            )
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .drawColoredShadow(
                shadowColor,
                alpha = SHADOW_ALPHA,
                shadowRadius = dp16
            )
    } else {
        Modifier.padding(
                top = dp6,
                bottom = dp8,
                start = dp16,
                end = dp16
            )
            .fillMaxWidth()
            .clickable(onClick = onClick)
    } // TODO fix too high api needed for this

    Card(
        elevation = noElevation,
        shape = RoundedCornerShape(cardCorners),
        modifier = modifier
    ) {
        val formattedWeightInGrams = "%.2f".format(item.weightInGrams)
        val formattedPriceDouble = "%.2f".format(item.priceDouble)
        val formattedPricePerOunce = "%.2f".format(item.pricePerOunce)
        val formattedPriceMarkup = "%.2f".format(item.priceMarkupInPercentage(xauPln.price))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.padding(dp12)) {
                if (item.image != null) {
                    ImageFromUrl(
                        item.image!!,
                        modifier = Modifier.width(imageWidth) // TODO make corners rounded
                    )
                }
            }
            Column(Modifier.padding(dp16)) {
                Text(text = item.title, fontWeight = FontWeight.Bold)
                Divider(color = dividerColor, thickness = dividerThickness)
                Row {
                    Text(
                        text = "$formattedPriceDouble zł",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = dp16)
                    )
                    Text(
                        text = "($formattedPricePerOunce/oz)",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(vectorResource(id = R.drawable.ic_gram), modifier = Modifier.padding(end = dp8))
                    Text(text = "$formattedWeightInGrams g", Modifier.padding(end = dp16))
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
