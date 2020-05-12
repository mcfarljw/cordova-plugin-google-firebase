const exec = require('cordova/exec')

module.exports = {
  admob: {
    onInterstitialClosed: function (callback) {
      exec(callback, callback, 'FirebasePlugin', 'onInterstitialClosed', [])
    },
    onRewardVideoClosed: function (callback) {
      exec(callback, callback, 'FirebasePlugin', 'onRewardVideoClosed', [])
    },
    onRewardVideoComplete: function (callback) {
      exec(callback, callback, 'FirebasePlugin', 'onRewardVideoComplete', [])
    },
    setup: function (appId, options) {
      options = options || {}

      options.interstitialId = options.interstitialId || 'ca-app-pub-3940256099942544/1033173712'
      options.rewardVideoId = options.rewardVideoId || 'ca-app-pub-3940256099942544/5224354917'
      options.testDevices = options.testDevices || []

      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobSetup', [appId, options.interstitialId, options.rewardVideoId, options.testDevices])
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
    showRewardVideo: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobShowRewardVideo', [])
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
  crashlytics: {},
  remoteConfig: {
    getArray: function (key) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'remoteConfigGetArray', [key])
        }
      )
    },
    getBoolean: function (key) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'remoteConfigGetBoolean', [key])
        }
      )
    },
    getNumber: function (key) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'remoteConfigGetNumber', [key])
        }
      )
    },
    getString: function (key) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'remoteConfigGetString', [key])
        }
      )
    },
    setup: function (interval) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'remoteConfigSetup', [interval || 43200])
        }
      )
    }
  }
}
