package com.ballisticapps.poetichelper.reward_ads_feature.domain.repository

import android.app.Activity

interface AdManagerRepository {
    fun loadRewardedAd(activity: Activity, callback: () -> Unit)
    fun showRewardedAd(activity: Activity, callback: AdCallback)
}
