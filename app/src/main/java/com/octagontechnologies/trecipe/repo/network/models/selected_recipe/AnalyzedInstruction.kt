package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class AnalyzedInstruction(
    @Json(name = "name")
    val name: String,
    @Json(name = "steps")
    val steps: List<Step>
)