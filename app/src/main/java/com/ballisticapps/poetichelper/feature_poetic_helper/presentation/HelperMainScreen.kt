package com.ballisticapps.poetichelper.feature_poetic_helper.presentation

import android.app.Activity
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ballisticapps.poetichelper.R
import com.ballisticapps.poetichelper.feature_poetic_helper.presentation.components.ArcRotationAnimation
import com.ballisticapps.poetichelper.feature_poetic_helper.presentation.components.TransparentHintTextField
import com.ballisticapps.poetichelper.feature_poetic_helper.presentation.viewmodel.HelperViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun HelperMainScreen(
    viewModel: HelperViewModel = hiltViewModel()
) {
    val promptState = viewModel.helperState.value.question
    val answer = viewModel.helperState.value.questionAIExplanation.response
    val infiniteTransition = rememberInfiniteTransition()
    val scope = rememberCoroutineScope()
    //main thread activity
     val activity = LocalContext.current as Activity


    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.monkey_logo),
            contentDescription = "Poetic Helper Logo",
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
        )

        Row(
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TransparentHintTextField(
                text = promptState,
                hint = "Enter desired question...",
                onValueChange = {
                    viewModel.onEvent(HelperEvent.EnteredPrompt(it))
                },
                onFocusChange = {
                    viewModel.onEvent(HelperEvent.EnteredPrompt(promptState))
                },
                isHintVisible = promptState.isBlank(),
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                modifier = Modifier
                    .onFocusEvent {}
                    .weight(.9f)
                    .padding(8.dp)
            )
            IconButton(
                onClick = {
                    GlobalScope.launch {
                        viewModel.onEvent(HelperEvent.ClickGeneratePoemButton(promptState, activity))
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_send_24),
                    contentDescription = "Send",
                    contentScale = ContentScale.Fit,
                )
            }
        }

        val scrollState = rememberScrollState()

        if (viewModel.helperState.value.questionAIExplanation.isLoading){
            ArcRotationAnimation(infiniteTransition = infiniteTransition)
        }

        if (!viewModel.helperState.value.questionAIExplanation.isLoading){
            Column(
                modifier = Modifier
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
                    .verticalScroll(scrollState, true),
            ) {
                Text(
                    text = answer,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 16.dp),
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }
        }
    }
}

