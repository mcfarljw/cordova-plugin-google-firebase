var app = {
  initialize: function() {
    if (window.cordova) {
      document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    } else {
      $(document).ready(this.onDeviceReady.bind(this))
    }

  },
  onDeviceReady: function() {
    if (window.plugins) {
      // if (window.device.platform === 'Android') {
      //   window.plugins.firebase.admob.setup('ca-app-pub-3940256099942544~3347511713', {
      //     interstitialId: 'ca-app-pub-3940256099942544/1033173712',
      //     rewardVideoId: 'ca-app-pub-3940256099942544/5224354917'
      //   })
      // }

      // if (window.device.platform === 'iOS') {
      //   window.plugins.firebase.admob.setup('ca-app-pub-3940256099942544~1458002511', {
      //     interstitialId: 'ca-app-pub-3940256099942544/4411468910',
      //     rewardVideoId: 'ca-app-pub-3940256099942544/1712485313'
      //   })
      // }

      // $('#show-interstitial').click(function () {
      //   window.plugins.firebase.admob.showInterstitial()
      // })

      // $('#show-rewarded-video').click(function () {
      //   window.plugins.firebase.admob.showRewardVideo()
      // })

      // window.plugins.firebase.admob.onInterstitialClosed(function (callback) {
      //   alert('EVENT: interstitial closed')
      // })

      // window.plugins.firebase.admob.onRewardVideoClosed(function (callback) {
      //   alert('EVENT: reward video closed')
      // })

      // window.plugins.firebase.admob.onRewardVideoComplete(function (callback) {
      //   alert('EVENT: reward video complete')
      // })

      window.plugins.firebase.remoteConfig.setup().then(function () {
        window.plugins.firebase.remoteConfig.getArray('TestArray').then(function (value) {
          $('#config-array').text(`[${value}]`)
        })

        window.plugins.firebase.remoteConfig.getBoolean('TestBoolean').then(function (value) {
          $('#config-boolean').text(value)
        })

        window.plugins.firebase.remoteConfig.getNumber('TestNumber').then(function (value) {
          $('#config-number').text(value)
        })

        window.plugins.firebase.remoteConfig.getString('TestString').then(function (value) {
          $('#config-string').text(`"${value}"`)
        })

        window.plugins.firebase.remoteConfig.getArray('InvalidArray').then(function (value) {
          $('#config-invalid-array').text(`[${value}]`)
        })

        window.plugins.firebase.remoteConfig.getBoolean('InvalidBoolean').then(function (value) {
          $('#config-invalid-boolean').text(value)
        })

        window.plugins.firebase.remoteConfig.getNumber('InvalidNumber').then(function (value) {
          $('#config-invalid-number').text(value)
        })

        window.plugins.firebase.remoteConfig.getString('InvalidString').then(function (value) {
          $('#config-invalid-string').text(`"${value}"`)
        })
      })
    }
  }
};

app.initialize();
