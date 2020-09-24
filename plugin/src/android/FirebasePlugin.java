package com.jernung.plugins.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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

  private Context applicationContext;
  private FirebaseAnalytics mAnalytics;
  private FirebaseCrashlytics mCrashlytics;
  private FirebaseRemoteConfig mRemoteConfig;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    applicationContext = cordova.getActivity().getApplicationContext();
    mAnalytics = FirebaseAnalytics.getInstance(applicationContext);
    mCrashlytics = FirebaseCrashlytics.getInstance();
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
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

  private void analyticsLogEvent(final String name, final JSONObject params) {
    try {
      final Bundle paramBundle = PluginUtils.jsonToBundle(params);

      cordova.getThreadPool().execute(() -> this.mAnalytics.logEvent(name, paramBundle));
    } catch (Exception error) {
      Log.e(PLUGIN_NAME, "Unable to log event!");
    }
  }

  private void analyticsSetScreenName(final String name) {
    Bundle bundle = new Bundle();

    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, name);
    bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");

    this.mAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
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
