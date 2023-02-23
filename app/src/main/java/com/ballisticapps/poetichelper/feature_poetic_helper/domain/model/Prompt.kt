package com.ballisticapps.poetichelper.feature_poetic_helper.domain.model

data class Prompt(
    val model: String,
    val prompt: String,
    val max_tokens: Int,
    val temperature: Int,
    val top_p: Int,
    val frequency_penalty: Int
){

}