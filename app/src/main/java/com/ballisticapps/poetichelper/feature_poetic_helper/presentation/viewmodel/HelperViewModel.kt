package com.ballisticapps.poetichelper.feature_poetic_helper.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ballisticapps.poetichelper.core.Resource
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Completion
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.model.Prompt
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.usecase.GetOpenAITextResponse
import com.ballisticapps.poetichelper.feature_poetic_helper.presentation.HelperEvent
import com.ballisticapps.poetichelper.reward_ads_feature.domain.repository.LoadRewardedAdUseCase
import com.google.android.gms.ads.OnUserEarnedRewardListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelperViewModel @Inject constructor(
    private val loadRewardedAdUseCase: LoadRewardedAdUseCase,
    private val getOpenAITextResponse: GetOpenAITextResponse,
    private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var helperState = mutableStateOf(HelperUIState())
        private set

    val _helperState = helperState.value

    suspend fun onEvent(event: HelperEvent) {
        when (event) {
            is HelperEvent.ClickGeneratePoemButton -> {
                viewModelScope.launch {
                    when (val resource =
                        loadRewardedAdUseCase.execute("ca-app-pub-3940256099942544/5224354917")) {
                        is Resource.Success -> {
                            // The rewarded ad was loaded successfully
                            val ad = resource.data

                            if (ad != null) {
                                ad.show(Application, object : RewardedAdCallback() {
                                    override fun onUserEarnedReward(reward: RewardItem) {
                                        // Called when the user has earned the reward
                                        // Make REST API call to handle the reward
                                        viewModelScope.launch {
                                            eventDataViewModel.sendRewardEvent()
                                        }
                                    }

                                    override fun onRewardedAdClosed() {
                                        // Called when the ad is closed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        Log.d(TAG, "Ad closed.")
                                        ad = null
                                    }

                                    override fun onRewardedAdFailedToShow(error: AdError?) {
                                        // Called when the ad fails to show.
                                        Log.e(TAG, "Ad failed to show.")
                                        ad = null
                                    }

                                    override fun onRewardedAdOpened() {
                                        // Called when the ad is shown.
                                        Log.d(TAG, "Ad shown.")
                                    }
                                })
                            }

                            // Set full-screen content callback for rewarded ad
                            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                                override fun onAdClicked() {
                                    // Called when a click is recorded for an ad.
                                    Log.d(TAG, "Ad was clicked.")
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed.
                                    // Set the ad reference to null so you don't show the ad a second time.
                                    Log.d(TAG, "Ad dismissed fullscreen content.")
                                    ad = null
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                    // Called when ad fails to show.
                                    Log.e(TAG, "Ad failed to show fullscreen content.")
                                    ad = null
                                }

                                override fun onAdImpression() {
                                    // Called when an impression is recorded for an ad.
                                    Log.d(TAG, "Ad recorded an impression.")
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    Log.d(TAG, "Ad showed fullscreen content.")
                                }
                            }
                        }
                        is Resource.Error -> {
                            Log.d(TAG, "Error loading rewarded ad: ${resource.message}")
                        }
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
                    "text-davinci-003",
                    "Answer the following in the form of a wise and creative poem: "
                            + helperState.value.question + "?",
                    256,
                    0,
                    1,
                    0
                )
            )

            result.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data as Completion

                        helperState.value = helperState.value.copy(
                            answer = result.data.choices[0].text,
                            questionAIExplanation = helperState.value.questionAIExplanation.copy(
                                response = result.data.choices[0].text,
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
    val question : String = "",
    val answer : String = "",
    val isAnswerVisible : Boolean = false,
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

