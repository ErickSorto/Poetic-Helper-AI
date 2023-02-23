package com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto

import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Completion

data class CompletionDTO(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)

fun CompletionDTO.toCompletion() = Completion(
    choices = choices,
    created = created,
    id = id,
    model = model,
    `object` = `object`,
    usage = usage
)