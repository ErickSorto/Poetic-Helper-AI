package com.ballisticapps.poetichelper.feature_poetic_helper.domain.model

import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.Choice
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.Usage

data class Completion(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)