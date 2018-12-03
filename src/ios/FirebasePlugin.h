#import <Cordova/CDVPlugin.h>
@import GoogleMobileAds;

@interface FirebasePlugin : CDVPlugin

@property(nonatomic, strong) NSString *applicationId;
@property(nonatomic, strong) GADInterstitial *interstitial;
@property(nonatomic, strong) NSString *interstitialId;
@property (nonatomic, strong) NSMutableArray *testDevices;

+ (FirebasePlugin *) firebasePlugin;

- (void)admobSetup:(CDVInvokedUrlCommand*)command;
- (void)admobShowInterstitial:(CDVInvokedUrlCommand*)command;
- (void)analyticsLogEvent:(CDVInvokedUrlCommand*)command;
- (void)analyticsSetUserId:(CDVInvokedUrlCommand*)command;
- (void)crashlyticsTest:(CDVInvokedUrlCommand*)command;
- (void)remoteConfigSetup:(CDVInvokedUrlCommand*)command;

@end
