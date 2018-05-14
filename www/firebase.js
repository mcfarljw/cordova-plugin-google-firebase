const exec = require('cordova/exec')

module.exports = {
  analytics: {
    logEvent: function (eventName, eventParams) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'logEvent', [eventName, eventParams || {}])
        }
      )
    },
    setUserId: function (userId) {
      return new Promise(
        function (resolve, reject) {
          exec(resolve, reject, 'FirebasePlugin', 'setUserId', [userId])
        }
      )
    }
  }
}
