package com.octagon_technologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class WinePairing(
    @Json(name = "pairedWines")
    val pairedWines: List<String>?,
    @Json(name = "pairingText")
    val pairingText: String?,
    @Json(name = "productMatches")
    val productMatches: List<ProductMatche>?
)