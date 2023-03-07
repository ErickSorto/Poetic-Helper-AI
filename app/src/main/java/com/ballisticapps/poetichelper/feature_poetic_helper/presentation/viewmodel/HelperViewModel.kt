package com.ballisticapps.poetichelper.feature_poetic_helper.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ballisticapps.poetichelper.core.Resource
import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.dto.Message
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.ChatCompletion
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Completion
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Prompt
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.usecase.GetOpenAITextResponse
import com.ballisticapps.poetichelper.feature_poetic_helper.presentation.HelperEvent
import com.ballisticapps.poetichelper.reward_ads_feature.domain.repository.AdCallback
import com.ballisticapps.poetichelper.reward_ads_feature.domain.repository.AdManagerRepository
import com.google.android.gms.ads.rewarded.RewardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelperViewModel @Inject constructor(
    private val adManagerRepository: AdManagerRepository,
    private val getOpenAITextResponse: GetOpenAITextResponse,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var helperState = mutableStateOf(HelperUIState())
        private set

    val _helperState = helperState.value


    fun onEvent(event: HelperEvent) {
        when (event) {
            is HelperEvent.ClickGeneratePoemButton -> {
                //make ad request
                event.activity.runOnUiThread {
                    adManagerRepository.loadRewardedAd(event.activity) {
                        //show ad
                        adManagerRepository.showRewardedAd(
                            event.activity,
                            object : AdCallback {
                                override fun onAdClosed() {
                                    //to be added later
                                }

                                override fun onAdRewarded(reward: RewardItem) {
                                    getAIResponse()
                                }

                                override fun onAdLeftApplication() {
                                    TODO("Not yet implemented")
                                }

                                override fun onAdLoaded() {
                                    TODO("Not yet implemented")
                                }

                                override fun onAdFailedToLoad(errorCode: Int) {
                                    //to be added later
                                }

                                override fun onAdOpened() {
                                    TODO("Not yet implemented")
                                }
                            })
                    }
                }
            }
            is HelperEvent.ClickRecordQuestionButton -> {
                //to be added later
            }
            is HelperEvent.EnteredPrompt -> {
                helperState.value = helperState.value.copy(question = event.value)
            }
        }
    }

    private fun getAIResponse() {
        viewModelScope.launch {
            val result = getOpenAITextResponse(
                Prompt(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        Message(
                            role = "user",
                            content = "Answer the following in the form of a wise and creative poem: " + helperState.value.question + "?"
                        )
                    ),
                    maxTokens = 256,
                    temperature = 0.7,
                    topP = 1.0,
                    presencePenalty = 0,
                    frequencyPenalty = 0,
                    user = "Poem seeker"
                )
            )

            result.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data as ChatCompletion

                        helperState.value = helperState.value.copy(
                            answer = result.data.choices[0].message.content,
                            questionAIExplanation = helperState.value.questionAIExplanation.copy(
                                response = result.data.choices[0].message.content,
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Error -> {
                        Log.d("HelperViewModel", "Error: ${result.message}")
                    }
                    is Resource.Loading -> {
                        //is loading is true
                        helperState.value = helperState.value.copy(
                            questionAIExplanation = helperState.value.questionAIExplanation.copy(
                                isLoading = true
                            )
                        )
                        Log.d("HelperViewModel", "Loading")
                    }
                }
            }
        }
    }
}

data class HelperUIState(
    val question: String = "",
    val answer: String = "",
    val isAnswerVisible: Boolean = false,
    val questionAIExplanation: QuestionAIExplanation = QuestionAIExplanation(
        response = "",
        isLoading = false,
        error = null
    )
) {
}

data class QuestionAIExplanation(
    val response: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

