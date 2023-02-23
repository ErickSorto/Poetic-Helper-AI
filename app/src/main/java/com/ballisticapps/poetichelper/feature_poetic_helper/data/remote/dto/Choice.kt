package com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto

data class Choice(
    val finish_reason: String,
    val index: Int,
    val logprobs: Any,
    val text: String
)