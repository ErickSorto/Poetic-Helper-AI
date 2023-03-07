package com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Choice(
    val index : Int,
    val message: Message,
    @SerializedName("finish_reason")
    val finishReason: String
)