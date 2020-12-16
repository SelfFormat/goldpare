package com.selfformat.goldpare.androidApp.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.selfformat.goldpare.androidApp.compose.ui.GoldpareTheme
import com.selfformat.goldpare.shared.model.GoldItem

@Composable
fun LazyGoldColumn(
    goldItems: List<GoldItem>
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val context = AmbientContext.current
        LazyColumnFor(goldItems) { item ->
            GoldRow(item, onClick = { openWebPage(item.link, context = context) })
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
        val formattedPriceMarkup = "%.2f".format(item.priceMarkup(6863.62))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column() {
                if (item.img_url != null) {
                    GlideSuperImage(item.img_url!!, modifier = Modifier.width(120.dp))
                }
            }
            Column(
                Modifier
                .padding(16.dp)) {
                Text(text = item.title, fontWeight = FontWeight.Bold)
                Text(text = "cena produktu: ${item.price.orEmpty()}")
                Text(text = "cena 1 uncji: $formattedPricePerOunce")
                Text(text = "waga w gramach: $formattedWeightInGrams")
                Text(text = "marża: $formattedPriceMarkup%")
                Text(text = "sklep: ${getWebsiteName(item.website)}")
                Text(text = "typ: ${item.type}")
                if (item.quantity > 1) {
                    Text(text = "sztuk w zestawie: ${item.quantity}")
                }
            }
        }
    }
}

@Composable
fun getWebsiteName(websiteKey: String): String {
    return when (websiteKey) {
        "goldenmark" -> "Goldenmark"
        "79element" -> "79th element"
        "mennicacompl" -> "Mennica Polska"
        "mennicakapitalowa" -> "Mennica Kapitałowa"
        "mennicakrajowa" -> "Mennica Krajowa"
        "mennicamazovia" -> "Mennica Mazovia"
        "metalelokacyjne" -> "Metale Lokacyjne"
        "metalmarketu" -> "Metal Market Europe"
        "wyrobymennicze" -> "Wyroby Mennicze"
        "mennicaskarbowa" -> "Mennica Skarbowa"
        "coininvest" -> "Coininvest"
        "travex" -> "Travex"
        "mennica24" -> "Mennica24"
        "zlotauncja" -> "Złota Uncja"
        "mennicakrakowska" -> "Mennica Krakowska"
        "mennicagdanska" -> "Mennica Gdańska"
        "mennicazielona" -> "Mennica Zielona"
        "goldco" -> "Goldco"
        "idfmetale" -> "IDF metale"
        "chojnackiikwiecien" -> "Chojnacki i Kwiecień"
        "flyingatomgold" -> "Flyingatom"
        else -> "Sklep"
    }
}

fun openWebPage(url: String, context: Context) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    ContextCompat.startActivity(context, intent, null)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val goldItem = GoldItem(
        1,
        "3000zł",
        "Gold 1/2 oz",
        "www.gold.com/1oz",
        "gold.com",
        "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg",
        weight = "1/4oz",
        quantity = 1,
        type = "coin"
    )
    val goldItem2 = GoldItem(
        1,
        "3000zł",
        "Gold 1/2 oz",
        "www.gold.com/1oz",
        "gold.com",
        "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg",
        weight = "1/4oz",
        quantity = 1,
        type = "coin"
    )
    val listOfGolds = listOf(goldItem, goldItem2)
    GoldpareTheme {
        LazyGoldColumn(goldItems = listOfGolds)
    }
}