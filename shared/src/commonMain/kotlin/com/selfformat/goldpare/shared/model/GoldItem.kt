package com.selfformat.goldpare.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class APIGoldItem(val price: String?, val title: String, val link: String, val website: String)

data class GoldItem(val id: Long, val price: String?, val title: String, val link: String, val website: String)
