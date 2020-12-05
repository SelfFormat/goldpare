package com.selfformat.goldpare.androidApp.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.selfformat.goldpare.androidApp.compose.ui.GoldpareTheme
import com.selfformat.goldpare.shared.GoldSDK
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainComposeActivity : AppCompatActivity() {

    private val mainScope = MainScope()
    private val sdk = GoldSDK(DatabaseDriverFactory(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainScope.launch {
            kotlin.runCatching {
                sdk.getGoldItems(true)
            }.onSuccess {
                setContent {
                    GoldpareTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            LazyGoldColumn(it)
                        }
                    }
                }
            }.onFailure {
                Toast.makeText(this@MainComposeActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}


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
    Card(Modifier
        .padding(8.dp)
        .preferredHeight(180.dp)
        .fillMaxWidth()
        .clickable(onClick = onClick)
    ) {
        Row() {
            if (item.img_url != null) {
                GlideSuperImage(item.img_url!!, contentScale = ContentScale.FillHeight)
            }
            Column(Modifier
                .padding(16.dp)) {
                Text(text = item.title, fontWeight = Bold)
                Text(text = item.price.orEmpty())
                Text(text = item.priceDouble.toString())
                Text(text = "price for ounce: ${item.pricePerOunce}")
                Text(text = "estimated weight: ${item.weight}")
                Text(text = item.website, modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

fun openWebPage(url: String, context: Context) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    startActivity(context, intent, null)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val goldItem = GoldItem(1, "3000zł", "Gold 1/2 oz", "www.gold.com/1oz", "gold.com", "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg", weight = "1/4oz")
    GoldpareTheme {
        GoldRow(goldItem) { }
    }
}