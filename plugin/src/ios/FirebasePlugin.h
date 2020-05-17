#import <Cordova/CDVPlugin.h>
@import Firebase;

@interface FirebasePlugin : CDVPlugin

@property(nonatomic, strong) FIRRemoteConfig *remoteConfig;

- (void)analyticsLogEvent:(CDVInvokedUrlCommand *)command;
- (void)analyticsSetScreenName:(CDVInvokedUrlCommand *)command;
- (void)analyticsSetUserId:(CDVInvokedUrlCommand *)command;
- (void)analyticsSetUserProperty:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigFetch:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigSetup:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetArray:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetBoolean:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetNumber:(CDVInvokedUrlCommand *)command;
- (void)remoteConfigGetString:(CDVInvokedUrlCommand *)command;

@end
