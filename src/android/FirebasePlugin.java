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

        self.applicationContext = cordova.getActivity().getApplicationContext();

        mAnalytics = FirebaseAnalytics.getInstance(applicationContext);
        mTestDeviceIds = new JSONArray();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("admobSetup".equals(action)) {
            String appId = args.getString(0);
            String interstitialId = args.getString(1);
            JSONArray testDevices = args.getJSONArray(1);

            admobSetup(appId, interstitialId, testDevices);

            return true;
        }


        if ("admobShowInterstitial".equals(action)) {
            showInterstitial();

            return true;
        }

        if ("analyticsLogEvent".equals(action)) {
            String name = args.getString(0);
            JSONObject params = args.getJSONObject(1);

            analyticsLogEvent(name, params);

            return true;
        }

        if ("analyticsSetScreenName".equals(action)) {
            return true;
        }

        if ("analyticsSetUserId".equals(action)) {
            String id = args.getString(0)

            analyticsSetUserId(id);

            return true;
        }

        if ("analyticsSetUserProperty".equals(action)) {
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

    private Boolean admobCanRequestNewAd() {
        return !mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading();
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

    private void admobSetup(final String appId, final String interstitialId, final JSONArray testDevices) {
        this.applicationId = appId;
        this.interstitialId = interstitialId

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                MobileAds.initialize(applicationContext, appId);

                mInterstitialAd = new InterstitialAd(applicationContext);

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

    private void analyticsSetUserId(final String id) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                mAnalytics.setUserId(id);
            }
        });
    }

    private void crashlyticsTest() {
        Crashlytics.getInstance().crash();
    }

    private void remoteConfigSetup() {}

}
