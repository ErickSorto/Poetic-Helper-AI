package com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)