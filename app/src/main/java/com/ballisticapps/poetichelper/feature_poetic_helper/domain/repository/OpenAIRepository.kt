package com.ballisticapps.poetichelper.feature_poetic_helper.domain.repository

import com.ballisticapps.poetichelper.core.Resource
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.CompletionDTO
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Prompt

interface OpenAIRepository {
    suspend fun getCompletion(
        prompt: Prompt
    ): Resource<CompletionDTO>
}