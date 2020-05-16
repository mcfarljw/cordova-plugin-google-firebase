#import <Cordova/CDVPlugin.h>
@import Firebase;

@interface FirebasePlugin : CDVPlugin

@property(nonatomic, strong) NSString *applicationId;
@property(nonatomic, strong) GADInterstitial *interstitial;
@property(nonatomic, strong) NSString *interstitialId;
@property(nonatomic, strong) NSString *interstitialClosedCallbackId;
@property(nonatomic, strong) FIRRemoteConfig *remoteConfig;
@property(nonatomic, strong) GADRewardedAd *rewardVideo;
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
- (void)onInterstitialClosed:(CDVInvokedUrlCommand *)command;
- (void)onRewardVideoClosed:(CDVInvokedUrlCommand *)command;
- (void)onRewardVideoComplete:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigFetch:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigSetup:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetArray:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetBoolean:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetNumber:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetString:(CDVInvokedUrlCommand *)command;

@end
