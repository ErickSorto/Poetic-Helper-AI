package com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto

data class Usage(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)