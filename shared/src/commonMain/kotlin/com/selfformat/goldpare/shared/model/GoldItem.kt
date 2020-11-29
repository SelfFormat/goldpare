package com.selfformat.goldpare.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class GoldItem(val price: String, val title: String, val link: String, val website: String)
