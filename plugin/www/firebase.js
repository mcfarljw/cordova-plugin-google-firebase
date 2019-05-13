const exec = require('cordova/exec')

module.exports = {
  admob: {
    setup: function (appId, interstitialId, testDevices) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobSetup', [appId, interstitialId, testDevices || []])
        }
      )
    },
    showInterstitial: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobShowInterstitial', [])
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
