#import <Cordova/CDVPlugin.h>
@import GoogleMobileAds;

@interface FirebasePlugin : CDVPlugin

@property(nonatomic, strong) NSString *applicationId;
@property(nonatomic, strong) GADInterstitial *interstitial;
@property(nonatomic, strong) NSString *interstitialId;
@property(nonatomic, strong) GADRewardBasedVideoAd *rewardedVideo;
@property(nonatomic, strong) NSString *rewardedVideoId;
@property(nonatomic, strong) NSMutableArray *testDevices;

- (void)admobSetup:(CDVInvokedUrlCommand*)command;
- (void)admobShowInterstitial:(CDVInvokedUrlCommand*)command;
- (void)admobShowRewardedVideo:(CDVInvokedUrlCommand*)command;
- (void)analyticsLogEvent:(CDVInvokedUrlCommand*)command;
- (void)analyticsSetScreenName:(CDVInvokedUrlCommand*)command;
- (void)analyticsSetUserId:(CDVInvokedUrlCommand*)command;
- (void)analyticsSetUserProperty:(CDVInvokedUrlCommand*)command;
- (void)crashlyticsTest:(CDVInvokedUrlCommand*)command;
- (void)remoteConfigSetup:(CDVInvokedUrlCommand*)command;

@end
