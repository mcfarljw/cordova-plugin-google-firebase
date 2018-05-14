const exec = require('cordova/exec')

module.exports = {
  admob: {
    addTestDevice: function (deviceId) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobAddTestDevice', [deviceId]);
        }
      )
    },
    getTestDevices: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobGetTestDevices', []);
        }
      )
    },
    requestInterstitial: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobRequestInterstitial', []);
        }
      )
    },
    setApplicationId: function (appId) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobSetAdmobAppId', [appId]);
        }
      )
    },
    setInterstitialId: function (unitId) {
      return new Promise(
        function(resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobSetInterstitialId', [unitId]);
        }
      )
    },
    showInterstitial: function () {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'admobShowInterstitial', []);
        }
      )
    }
  },
  analytics: {
    logEvent: function (eventName, eventParams) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'analyticsLogEvent', [eventName, eventParams || {}])
        }
      )
    },
    setUserId: function (userId) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'analyticsSetUserId', [userId])
        }
      )
    }
  }
}
