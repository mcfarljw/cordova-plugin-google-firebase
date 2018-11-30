package com.jernung.plugins.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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
    private FirebaseAnalytics mAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private InterstitialAd mInterstitialAd;
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
        // ADMOB
        if ("admobAddTestDevice".equals(action)) {
            admobAddTestDevice(args.getString(0));

            callbackContext.success(admobGetTestDevices());

            return true;
        }

        if ("admobGetTestDevices".equals(action)) {
            callbackContext.success(admobGetTestDevices());

            return true;
        }

        if ("admobRequestInterstitial".equals(action)) {
            admobRequestNewInterstitial();

            return true;
        }

        if ("admobSetAdmobAppId".equals(action)) {
            admobSetAdmobAppId(args.getString(0));

            return true;
        }

        if ("admobSetInterstitialId".equals(action)) {
            admobSetInterstitialId(args.getString(0));

            return true;
        }

        // ANALYTICS
        if ("admobShowInterstitial".equals(action)) {
            showInterstitial();

            return true;
        }

        if ("analyticsLogEvent".equals(action)) {
            String eventName = args.getString(0);
            JSONObject eventParams = args.getJSONObject(1);

            analyticsLogEvent(eventName, eventParams);

            return true;
        }

        if ("analyticsSetUserId".equals(action)) {
            analyticsSetUserId(args.getString(0));

            return true;
        }

        // CRASHLYTICS
        if ("crashlyticsTest".equals(action)) {
            crashlyticsTest();

            return true;
        }

        // REMOTE CONFIG
        if ("remoteConfigSetup".equals(action)) {
            remoteConfigSetup();

            return true;
        }

        return false;
    }

    private void admobAddTestDevice(final String deviceId) {
        mTestDeviceIds.put(deviceId);
    }

    private Boolean admobCanRequestNewAd() {
        return !mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading();
    }

    private JSONArray admobGetTestDevices() {
        return mTestDeviceIds;
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

                if (admobCanRequestNewAd()) {
                    mInterstitialAd.loadAd(adRequest.build());
                }
            }
        });
    }

    private void admobSetAdmobAppId(final String appId) {
        this.applicationId = appId;

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                MobileAds.initialize(applicationContext, appId);
            }
        });
    }

    private void admobSetInterstitialId(final String interstitialId) {
        this.interstitialId = interstitialId;

        if (mInterstitialAd == null) {
            mInterstitialAd = new InterstitialAd(applicationContext);
        }

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mInterstitialAd.setAdUnitId(interstitialId);
                mInterstitialAd.setAdListener(
                        new AdListener() {
                            @Override
                            public void onAdClosed() {
                                admobRequestNewInterstitial();
                            }
                        }
                );

                admobRequestNewInterstitial();
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

    private void analyticsLogEvent(final String eventName, final JSONObject eventParams) {
        try {
            final Bundle params = PluginUtils.jsonToBundle(eventParams);

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    mAnalytics.logEvent(eventName, params);
                }
            });
        } catch (Exception error) {
            Log.e(PLUGIN_NAME, error.getMessage());
        }
    }

    private void analyticsSetUserId(final String userId) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                mAnalytics.setUserId(userId);
            }
        });
    }

    private void crashlyticsTest() {
        Crashlytics.getInstance().crash();
    }

    private void remoteConfigSetup() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

}
