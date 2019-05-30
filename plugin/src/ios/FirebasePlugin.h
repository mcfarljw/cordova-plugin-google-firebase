#import <Cordova/CDVPlugin.h>
@import GoogleMobileAds;

@interface FirebasePlugin : CDVPlugin

@property(nonatomic, strong) NSString *applicationId;
@property(nonatomic, strong) GADInterstitial *interstitial;
@property(nonatomic, strong) NSString *interstitialId;
@property(nonatomic, strong) NSString *interstitialClosedCallbackId;
@property(nonatomic, strong) GADRewardBasedVideoAd *rewardVideo;
@property(nonatomic, strong) NSString *rewardVideoClosedCallbackId;
@property(nonatomic, strong) NSString *rewardVideoCompleteCallbackId;
@property(nonatomic, strong) NSString *rewardVideoId;
@property(nonatomic, strong) NSMutableArray *testDevices;

- (void)admobSetup:(CDVInvokedUrlCommand *)command;
- (void)admobShowInterstitial:(CDVInvokedUrlCommand *)command;
- (void)admobShowRewardVideo:(CDVInvokedUrlCommand *)command;
- (void)analyticsLogEvent:(CDVInvokedUrlCommand *)command;
- (void)analyticsSetScreenName:(CDVInvokedUrlCommand *)command;
- (void)analyticsSetUserId:(CDVInvokedUrlCommand *)command;
- (void)analyticsSetUserProperty:(CDVInvokedUrlCommand *)command;
- (void)crashlyticsTest:(CDVInvokedUrlCommand *)command;
- (void)onInterstitialClosed:(CDVInvokedUrlCommand *)command;
- (void)onRewardVideoClosed:(CDVInvokedUrlCommand *)command;
- (void)onRewardVideoComplete:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigSetup:(CDVInvokedUrlCommand *)command;

@end
