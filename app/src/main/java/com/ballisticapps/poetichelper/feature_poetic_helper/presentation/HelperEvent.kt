package com.ballisticapps.poetichelper.feature_poetic_helper.presentation

sealed class HelperEvent {
    data class ClickGeneratePoemButton(val value: String) : HelperEvent()
    data class ClickRecordQuestionButton(val value: String) : HelperEvent()
    data class EnteredPrompt(val value: String) : HelperEvent()
}