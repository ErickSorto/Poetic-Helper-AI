package com.ballisticapps.poetichelper.feature_poetic_helper.data.remote

import com.ballisticapps.poetichelper.BuildConfig
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.CompletionDTO
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Prompt
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAITextApi {

    @Headers("Content-Type: application/json", "Authorization: Bearer " + BuildConfig.API_KEY)
    @POST("completions")
    suspend fun getCompletion(@Body prompt: Prompt): Response<CompletionDTO>
}