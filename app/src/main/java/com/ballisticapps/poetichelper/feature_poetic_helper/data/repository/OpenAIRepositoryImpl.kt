package com.ballisticapps.poetichelper.feature_poetic_helper.data.repository

import android.util.Log
import com.ballisticapps.poetichelper.core.Resource
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.OpenAITextApi
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.CompletionDTO
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Prompt
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.repository.OpenAIRepository
import javax.inject.Inject

class OpenAIRepositoryImpl @Inject constructor(
    private val api: OpenAITextApi
): OpenAIRepository {

    override suspend fun getCompletion(
        prompt: Prompt
    ): Resource<CompletionDTO> {

        var result = api.getCompletion(prompt)
        if(result.isSuccessful){
            return Resource.Success(data = result.body()!!)
        }else{
            Log.d("GetOpenAITextResponse", "${result.code()} ${result.message()}, ${result.body()}, ${result.headers()}")
            return Resource.Error(message = result.message())
        }
    }
}