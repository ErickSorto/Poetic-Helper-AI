package com.ballisticapps.poetichelper.feature_poetic_helper.domain.usecase

import android.util.Log
import com.ballisticapps.poetichelper.core.Resource
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.toCompletion
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Prompt
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.repository.OpenAIRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import javax.inject.Inject

class GetOpenAITextResponse @Inject constructor(
    private val repository: OpenAIRepository,
) {

    operator fun invoke(prompt: Prompt) = flow {
        emit(Resource.Loading<String>())
        val response = repository.getCompletion(prompt)
        when(response){
            is Resource.Success -> {
                emit(Resource.Success(response.data!!.toCompletion()))
            }
            is Resource.Error -> {
                Log.d("GetOpenAITextResponse", response.message.toString())
                emit(Resource.Error(response.message!!))
            }
            else -> {

            }
        }
    }.onStart {
        emit(Resource.Loading())
    }.catch {
        when(it){
            is HttpException -> {
                Log.d("GetOpenAITextResponse", "HttpException")
                emit(Resource.Error("http exception.. ${it.message()}"))
            }
            else -> {
                Log.d("GetOpenAITextResponse", "Exception")
                emit(Resource.Error("some exception happened.."))
            }
        }
    }
}