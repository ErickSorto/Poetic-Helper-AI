package com.ballisticapps.poetichelper.feature_poetic_helper.data.repository

import android.util.Log
import com.ballisticapps.poetichelper.core.Resource
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.OpenAITextApi
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.CompletionDTO
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.Message
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.toChatCompletion
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.ChatCompletion
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Prompt
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.repository.OpenAIRepository
import javax.inject.Inject

class OpenAIRepositoryImpl @Inject constructor(private val api: OpenAITextApi) : OpenAIRepository {

    override suspend fun getChatCompletion(prompt: Prompt): Resource<ChatCompletion> {

        return try {
            val response = api.getCompletion(prompt)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it.toChatCompletion())
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}