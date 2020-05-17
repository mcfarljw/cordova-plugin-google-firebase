package com.jernung.plugins.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirebasePlugin extends CordovaPlugin {

  private static final String PLUGIN_NAME = "FirebasePlugin";

  private String applicationId;
  private String interstitialId;
  private String rewardVideoId;

  private Context applicationContext;
  private CallbackContext interstitialClosedCallback;
  private CallbackContext rewardVideoClosedCallback;
  private CallbackContext rewardVideoCompleteCallback;
  private FirebaseAnalytics mAnalytics;
  private FirebaseCrashlytics mCrashlytics;
  private InterstitialAd mInterstitialAd;
  private FirebaseRemoteConfig mRemoteConfig;
  private RewardedAd mRewardVideoAd;
  private Boolean testing;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    applicationContext = cordova.getActivity().getApplicationContext();
    mAnalytics = FirebaseAnalytics.getInstance(applicationContext);
    mCrashlytics = FirebaseCrashlytics.getInstance();
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if ("admobSetup".equals(action)) {
      String appId = args.getString(0);
      String interstitialId = args.getString(1);
      String rewardedVideoId = args.getString(2);
      Boolean testing = args.getBoolean(3);

      this.admobSetup(appId, interstitialId, rewardedVideoId, testing);

      return true;
    }

    if ("admobShowInterstitial".equals(action)) {
      this.showInterstitial();

      return true;
    }

    if ("admobShowRewardVideo".equals(action)) {
      this.showRewardVideo();

      return true;
    }

    if ("analyticsLogEvent".equals(action)) {
      String name = args.getString(0);
      JSONObject params = args.getJSONObject(1);

      this.analyticsLogEvent(name, params);

      return true;
    }

    if ("analyticsSetScreenName".equals(action)) {
      String name = args.getString(0);

      this.analyticsSetScreenName(name);

      return true;
    }

    if ("analyticsSetUserId".equals(action)) {
      String id = args.getString(0);

      this.analyticsSetUserId(id);

      return true;
    }

    if ("analyticsSetUserProperty".equals(action)) {
      String name = args.getString(0);
      String value = args.getString(1);

      this.analyticsSetUserProperty(name, value);

      return true;
    }

    if ("onInterstitialClosed".equals(action)) {
      this.interstitialClosedCallback = callbackContext;

      return true;
    }

    if ("onRewardVideoClosed".equals(action)) {
      this.rewardVideoClosedCallback = callbackContext;

      return true;
    }

    if ("onRewardVideoComplete".equals(action)) {
      this.rewardVideoCompleteCallback = callbackContext;

      return true;
    }

    if ("remoteConfigSetup".equals(action)) {
      long interval = args.getLong(0);

      this.remoteConfigSetup(interval, callbackContext);

      return true;
    }

    if ("remoteConfigGetArray".equals(action)) {
      String key = args.getString(0);

      this.remoteConfigGetArray(key, callbackContext);

      return true;
    }

    if ("remoteConfigGetBoolean".equals(action)) {
      String key = args.getString(0);

      this.remoteConfigGetBoolean(key, callbackContext);

      return true;
    }

    if ("remoteConfigGetNumber".equals(action)) {
      String key = args.getString(0);

      this.remoteConfigGetNumber(key, callbackContext);

      return true;
    }

    if ("remoteConfigGetString".equals(action)) {
      String key = args.getString(0);

      this.remoteConfigGetString(key, callbackContext);

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
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);

      pluginResult.setKeepCallback(true);

      admobRequestNewInterstitial();

      cordova.getActivity().runOnUiThread(() -> interstitialClosedCallback.sendPluginResult(pluginResult));
    }
  }

  private void admobRequestNewInterstitial() {
    if (this.mInterstitialAd != null && this.mInterstitialAd.isLoaded()) {
      return;
    }

    cordova.getActivity().runOnUiThread(() -> this.mInterstitialAd.loadAd(new AdRequest.Builder().build()));
  }

  private void admobRequestNewRewardedVideo() {
    if (this.mRewardVideoAd != null && this.mRewardVideoAd.isLoaded()) {
      return;
    }

    cordova.getActivity().runOnUiThread(() -> {
      mRewardVideoAd = new RewardedAd(this.applicationContext, this.rewardVideoId);

      mRewardVideoAd.loadAd(new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
        @Override
        public void onRewardedAdLoaded() {}

        @Override
        public void onRewardedAdFailedToLoad(int errorCode) {}
      });
    });
  }

  private void admobSetup(final String appId, final String interstitialId, final String rewardVideoId, final Boolean testing) {
    this.applicationId = appId;
    this.interstitialId = interstitialId;
    this.rewardVideoId = rewardVideoId;
    this.testing = testing;

    if (this.testing) {
      this.applicationId = "ca-app-pub-3940256099942544~3347511713";
      this.interstitialId = "ca-app-pub-3940256099942544/1033173712";
      this.rewardVideoId = "ca-app-pub-3940256099942544/5224354917";
    }

    cordova.getActivity().runOnUiThread(() -> MobileAds.initialize(this.applicationContext, (InitializationStatus initializationStatus) -> {
      this.mInterstitialAd = new InterstitialAd(this.applicationContext);
      this.mInterstitialAd.setAdUnitId(this.interstitialId);
      this.mInterstitialAd.setAdListener(new InterstitialListener());

      this.admobRequestNewInterstitial();
      this.admobRequestNewRewardedVideo();
    }));
  }

  private void showInterstitial() {
    cordova.getActivity().runOnUiThread(() -> {
      if (this.mInterstitialAd.isLoaded()) {
        this.mInterstitialAd.show();
      } else {
        this.admobRequestNewInterstitial();
      }
    });
  }

  private void showRewardVideo() {
    cordova.getActivity().runOnUiThread(() -> {
      if (this.mRewardVideoAd.isLoaded()) {
        this.mRewardVideoAd.show(cordova.getActivity(), new RewardedAdCallback() {
          @Override
          public void onRewardedAdOpened() {}

          @Override
          public void onRewardedAdClosed() {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);

            pluginResult.setKeepCallback(true);

            admobRequestNewRewardedVideo();

            cordova.getActivity().runOnUiThread(() -> rewardVideoClosedCallback.sendPluginResult(pluginResult));
          }

          @Override
          public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);

            pluginResult.setKeepCallback(true);

            cordova.getActivity().runOnUiThread(() -> rewardVideoCompleteCallback.sendPluginResult(pluginResult));
          }

          @Override
          public void onRewardedAdFailedToShow(int errorCode) {}
        });
      } else {
        this.admobRequestNewInterstitial();
      }
    });
  }

  private void analyticsLogEvent(final String name, final JSONObject params) {
    try {
      final Bundle paramBundle = PluginUtils.jsonToBundle(params);

      cordova.getThreadPool().execute(() -> this.mAnalytics.logEvent(name, paramBundle));
    } catch (Exception error) {
      Log.e(PLUGIN_NAME, error.getMessage());
    }
  }

  private void analyticsSetScreenName(final String name) {
    this.mAnalytics.setCurrentScreen(cordova.getActivity(), name, null);
  }

  private void analyticsSetUserId(final String id) {
    this.mAnalytics.setUserId(id);
  }

  private void analyticsSetUserProperty(final String name, final String value) {
    this.mAnalytics.setUserProperty(name, value);
  }

  private void remoteConfigSetup(final long interval, final CallbackContext callbackContext) {
    FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
      .setFetchTimeoutInSeconds(interval)
      .build();

    this.mRemoteConfig = FirebaseRemoteConfig.getInstance();
    this.mRemoteConfig.setConfigSettingsAsync(settings);
    this.mRemoteConfig.fetchAndActivate()
      .addOnCompleteListener(cordova.getActivity(), task -> {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR);

        if (task.isSuccessful()) {
          pluginResult = new PluginResult(PluginResult.Status.OK);
        }

        callbackContext.sendPluginResult(pluginResult);
      });
  }

  private void remoteConfigGetArray(final String key, final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(() -> {
      FirebaseRemoteConfigValue value = this.mRemoteConfig.getValue(key);
      PluginResult pluginResult;

      try {
        String byteString = new String(value.asByteArray());
        JSONArray byteArray = new JSONArray(byteString);

        pluginResult = new PluginResult(PluginResult.Status.OK, byteArray);
      } catch (JSONException error) {
        Log.d(PLUGIN_NAME, error.getMessage());

        pluginResult = new PluginResult(PluginResult.Status.OK, new JSONArray());
      }

      callbackContext.sendPluginResult(pluginResult);
    });
  }

  private void remoteConfigGetBoolean(final String key, final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(() -> {
      FirebaseRemoteConfigValue value = this.mRemoteConfig.getValue(key);
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, value.asBoolean());

      callbackContext.sendPluginResult(pluginResult);
    });
  }

  private void remoteConfigGetNumber(final String key, final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(() -> {
      FirebaseRemoteConfigValue value = this.mRemoteConfig.getValue(key);
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, (float) value.asDouble());

      callbackContext.sendPluginResult(pluginResult);
    });
  }

  private void remoteConfigGetString(final String key, final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(() -> {
      FirebaseRemoteConfigValue value = this.mRemoteConfig.getValue(key);
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, value.asString());

      callbackContext.sendPluginResult(pluginResult);
    });
  }

}
