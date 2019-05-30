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
          window.plugins.firebase.admob.showRewardedVideo()
        })
      }
    }
};

app.initialize();
