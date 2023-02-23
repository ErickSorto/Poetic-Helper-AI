package com.ballisticapps.poetichelper.reward_ads_feature.domain.repository

import com.ballisticapps.poetichelper.core.Resource
import com.google.android.gms.ads.rewarded.RewardedAd

interface LoadRewardedAdUseCase {
    suspend fun execute(adUnitId: String): Resource<RewardedAd>
}
