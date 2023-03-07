package com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto

import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.ChatCompletion
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Completion
import com.google.gson.annotations.SerializedName

data class CompletionDTO(
    val id: String,
    @SerializedName("object")
    val objectValue: String,
    val created: Int,
    val choices: List<Choice>,
    val usage: Usage
)

fun CompletionDTO.toChatCompletion() = ChatCompletion(
    id = id,
    objectValue = objectValue,
    created = created,
    choices = choices.map {
        Choice(
            index = it.index,
            message = it.message,
            finishReason = it.finishReason
        )
    },
    usage = Usage(
        promptTokens = usage.promptTokens,
        completionTokens = usage.completionTokens,
        totalTokens = usage.totalTokens
    )
)