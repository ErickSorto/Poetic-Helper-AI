package com.ballisticapps.poetichelper.reward_ads_feature.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.ballisticapps.poetichelper.core.Resource
import com.ballisticapps.poetichelper.reward_ads_feature.domain.repository.LoadRewardedAdUseCase
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdMobLoadRewardedAdUseCase(private val context: Context) : LoadRewardedAdUseCase {
    override suspend fun execute(adUnitId: String): Resource<RewardedAd> {
        val adRequest = AdRequest.Builder().build()

        return try {
            withContext(Dispatchers.IO) {
                var adResult: Resource<RewardedAd> = Resource.Loading(null)
                RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        // The rewarded ad failed to load
                        Log.e(TAG, "Rewarded ad failed to load: $adError")
                        adResult = Resource.Error("Rewarded ad failed to load: $adError", null)
                    }

                    override fun onAdLoaded(ad: RewardedAd) {
                        // The rewarded ad was successfully loaded
                        Log.d(TAG, "Rewarded ad loaded successfully")
                        adResult = Resource.Success(ad)
                    }
                })
                adResult
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load rewarded ad", null)
        }
    }
}

