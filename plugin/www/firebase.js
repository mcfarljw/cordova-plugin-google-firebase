const exec = require('cordova/exec')

module.exports = {
  admob: {
    setup: function (appId, options, testDevices) {
      options = options || {}

      options.interstitialId = options.interstitialId || 'ca-app-pub-3940256099942544/1033173712'
      options.rewardedVideoId = options.rewardedVideoId || 'ca-app-pub-3940256099942544/5224354917'
      options.testDevices = options.testDevices || []

      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobSetup', [appId, options.interstitialId, options.rewardedVideoId, options.testDevices])
        }
      )
    },
    showInterstitial: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobShowInterstitial', [])
        }
      )
    },
    showRewardedVideo: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobShowRewardedVideo', [])
        }
      )
    }
  },
  analytics: {
    logEvent: function (name, params) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'analyticsLogEvent', [name, params || {}])
        }
      )
    },
    setScreenName: function (name) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'analyticsSetScreenName', [name])
        }
      )
    },
    setUserId: function (id) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'analyticsSetUserId', [id])
        }
      )
    },
    setUserProperty: function (name, value) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'analyticsSetUserProperty', [name, value])
        }
      )
    }
  },
  crashlytics: {
    test: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'crashlyticsTest', [])
        }
      )
    }
  },
  remoteConfig: {
    setup: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'remoteConfigSetup', [])
        }
      )
    }
  }
}
