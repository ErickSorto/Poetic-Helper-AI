package com.ballisticapps.poetichelper.feature_poetic_helper.presentation.state


data class HelperTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
) {
}
