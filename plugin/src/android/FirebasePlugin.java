package com.jernung.plugins.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirebasePlugin extends CordovaPlugin {

    private static final String PLUGIN_NAME = "FirebasePlugin";

    private Context applicationContext;
    private String applicationId;
    private String interstitialId;
    private String rewardedVideoId;
    private FirebaseAnalytics mAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private JSONArray mTestDeviceIds;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        applicationContext = cordova.getActivity().getApplicationContext();

        mAnalytics = FirebaseAnalytics.getInstance(applicationContext);
        mTestDeviceIds = new JSONArray();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("admobSetup".equals(action)) {
            String appId = args.getString(0);
            String interstitialId = args.getString(1);
            String rewardedVideoId = args.getString(2);
            JSONArray testDevices = args.getJSONArray(3);

            admobSetup(appId, interstitialId, rewardedVideoId, testDevices);

            return true;
        }

        if ("admobShowInterstitial".equals(action)) {
            showInterstitial();

            return true;
        }

        if ("admobShowRewardedVideo".equals(action)) {
            showRewardedVideo();

            return true;
        }

        if ("analyticsLogEvent".equals(action)) {
            String name = args.getString(0);
            JSONObject params = args.getJSONObject(1);

            analyticsLogEvent(name, params);

            return true;
        }

        if ("analyticsSetScreenName".equals(action)) {
            String name = args.getString(0);

            analyticsSetScreenName(name);

            return true;
        }

        if ("analyticsSetUserId".equals(action)) {
            String id = args.getString(0);

            analyticsSetUserId(id);

            return true;
        }

        if ("analyticsSetUserProperty".equals(action)) {
            String name = args.getString(0);
            String value = args.getString(1);

            analyticsSetUserProperty(name, value);

            return true;
        }

        if ("crashlyticsTest".equals(action)) {
            crashlyticsTest();

            return true;
        }

        if ("remoteConfigSetup".equals(action)) {
            remoteConfigSetup();

            return true;
        }

        return false;
    }

    private class InterstitialListener extends AdListener {
        @Override
        public void onAdFailedToLoad(int errorCode) {}

        @Override
        public void onAdLeftApplication() {}

        @Override
        public void onAdLoaded() {}

        @Override
        public void onAdOpened() {}

        @Override
        public void onAdClosed() {
            admobRequestNewInterstitial();
        }
    }

    private class RewardedListener implements RewardedVideoAdListener {
        @Override
        public void onRewardedVideoAdLoaded() {}

        @Override
        public void onRewardedVideoAdOpened() {}

        @Override
        public void onRewardedVideoStarted() {}

        @Override
        public void onRewardedVideoAdClosed() { admobRequestNewRewardedVideo(); }

        @Override
        public void onRewarded(RewardItem rewardItem) {}

        @Override
        public void onRewardedVideoAdLeftApplication() {}

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {}

        @Override
        public void onRewardedVideoCompleted() {}
    }

    private void admobRequestNewInterstitial() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AdRequest.Builder adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

                try {
                    for (int i = 0; i < mTestDeviceIds.length(); i++) {
                        adRequest.addTestDevice(mTestDeviceIds.getString(i));
                    }
                } catch (JSONException error) {
                    Log.e(PLUGIN_NAME, error.getMessage());
                }

                if (!mInterstitialAd.isLoaded()) {
                    mInterstitialAd.loadAd(adRequest.build());
                }
            }
        });
    }

    private void admobRequestNewRewardedVideo() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AdRequest.Builder adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

                try {
                    for (int i = 0; i < mTestDeviceIds.length(); i++) {
                        adRequest.addTestDevice(mTestDeviceIds.getString(i));
                    }
                } catch (JSONException error) {
                    Log.e(PLUGIN_NAME, error.getMessage());
                }

                if (!mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.loadAd(rewardedVideoId, adRequest.build());
                }
            }
        });
    }

    private void admobSetup(final String appId, final String interstitialId, final String rewardedVideoId, final JSONArray testDevices) {
        this.applicationId = appId;
        this.interstitialId = interstitialId;
        this.rewardedVideoId = rewardedVideoId;
        this.mTestDeviceIds = testDevices;

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                MobileAds.initialize(applicationContext, appId);

                mInterstitialAd = new InterstitialAd(applicationContext);
                mInterstitialAd.setAdUnitId(interstitialId);
                mInterstitialAd.setAdListener(new InterstitialListener());

                mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(applicationContext);
                mRewardedVideoAd.setRewardedVideoAdListener(new RewardedListener());

                admobRequestNewInterstitial();
                admobRequestNewRewardedVideo();
            }
        });
    }

    private void showInterstitial() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    admobRequestNewInterstitial();
                }
            }
        });
    }

    private void showRewardedVideo() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    admobRequestNewInterstitial();
                }
            }
        });
    }

    private void analyticsLogEvent(final String name, final JSONObject params) {
        try {
            final Bundle paramBundle = PluginUtils.jsonToBundle(params);

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    mAnalytics.logEvent(name, paramBundle);
                }
            });
        } catch (Exception error) {
            Log.e(PLUGIN_NAME, error.getMessage());
        }
    }

    private void analyticsSetScreenName(final String name) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                mAnalytics.setCurrentScreen(cordova.getActivity(), name, null);
            }
        });
    }

    private void analyticsSetUserId(final String id) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                mAnalytics.setUserId(id);
            }
        });
    }

    private void analyticsSetUserProperty(final String name, final String value) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                mAnalytics.setUserProperty(name, value);
            }
        });
    }

    private void crashlyticsTest() {
        Crashlytics.getInstance().crash();
    }

    private void remoteConfigSetup() {}

}
