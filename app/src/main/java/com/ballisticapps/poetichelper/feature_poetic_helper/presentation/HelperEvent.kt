package com.ballisticapps.poetichelper.feature_poetic_helper.presentation

import android.app.Activity
import android.content.Context

sealed class HelperEvent {
    data class ClickGeneratePoemButton(val value: String, val activity : Activity) : HelperEvent()
    data class ClickRecordQuestionButton(val value: String) : HelperEvent()
    data class EnteredPrompt(val value: String) : HelperEvent()
}