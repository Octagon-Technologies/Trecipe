package com.octagon_technologies.trecipe.domain.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    val stepNumber: Int,
    val step: String
) : Parcelable