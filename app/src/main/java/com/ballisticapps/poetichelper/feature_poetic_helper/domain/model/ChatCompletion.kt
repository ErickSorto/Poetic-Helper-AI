package com.ballisticapps.poetichelper.feature_poetic_helper.domain.model

import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.Choice
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.Usage
import com.google.gson.annotations.SerializedName

data class ChatCompletion(
    val id: String,
    @SerializedName("object")
    val objectValue: String,
    val created: Int,
    val choices: List<Choice>,
    val usage: Usage
)