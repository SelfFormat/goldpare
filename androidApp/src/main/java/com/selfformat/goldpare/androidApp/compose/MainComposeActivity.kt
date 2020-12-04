package com.selfformat.goldpare.androidApp.compose

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                Toast.makeText(this@MainComposeActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Composable
fun LazyGoldColumn(
        goldItems: List<GoldItem>,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumnFor(goldItems) { item ->
            GoldRow(item)
        }
    }
}


@Composable
fun GoldRow(item: GoldItem) {
    Card(Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
        Column(Modifier
                .padding(16.dp)) {
            Text(text = item.title)
            Text(text = item.price)
            Text(text = item.link)
            Text(text = item.website)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val goldItem = GoldItem(1, "Gold 1oz", "3000zł", "www.gold.com/1oz", "gold.com")
    GoldpareTheme {
        GoldRow(goldItem)
    }
}