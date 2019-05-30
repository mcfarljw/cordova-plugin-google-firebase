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
        if (window.device.platform === 'Android') {
          window.plugins.firebase.admob.setup('ca-app-pub-3940256099942544~3347511713', {
            interstitialId: 'ca-app-pub-3940256099942544/1033173712'
          })
        }

        if (window.device.platform === 'iOS') {
          window.plugins.firebase.admob.setup('ca-app-pub-3940256099942544~1458002511', {
            interstitialId: 'ca-app-pub-3940256099942544/4411468910'
          })
        }

        $('#show-interstitial').click(function () {
          window.plugins.firebase.admob.showInterstitial()
        })

        $('#show-rewarded-video').click(function () {
          window.plugins.firebase.admob.showRewardVideo()
        })

        window.plugins.firebase.admob.onInterstitialClosed(function (callback) {
          console.log('EVENT', 'interstitial closed')
        })

        window.plugins.firebase.admob.onRewardVideoClosed(function (callback) {
          console.log('EVENT', 'reward video closed')
        })

        window.plugins.firebase.admob.onRewardVideoComplete(function (callback) {
          console.log('EVENT', 'reward video complete')
        })
      }
    }
};

app.initialize();
