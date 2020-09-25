const exec = require('cordova/exec')

module.exports = {
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
  dynamicLinks: {
    onData: function (callback) {
      exec(callback, null, 'FirebasePlugin', 'dynamicLinksData', [])
    }
  },
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
