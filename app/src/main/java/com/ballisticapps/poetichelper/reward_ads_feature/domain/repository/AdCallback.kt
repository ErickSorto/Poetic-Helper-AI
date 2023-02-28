package com.ballisticapps.poetichelper.reward_ads_feature.domain.repository

import com.google.android.gms.ads.rewarded.RewardItem

interface AdCallback {
    fun onAdLoaded()
    fun onAdFailedToLoad(errorCode: Int)
    fun onAdOpened()
    fun onAdClosed()
    fun onAdRewarded(reward: RewardItem)
    fun onAdLeftApplication()
}