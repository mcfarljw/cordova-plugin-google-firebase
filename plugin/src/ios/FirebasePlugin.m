#import "FirebasePlugin.h"
#import "Firebase.h"
#import "AppDelegate.h"
#import <Cordova/CDVPlugin.h>
#import <Crashlytics/Crashlytics.h>
#import <Fabric/Fabric.h>

@implementation FirebasePlugin

- (void)admobRequestInterstitial {
    [self.commandDelegate runInBackground:^{
        self.interstitial = [[GADInterstitial alloc] initWithAdUnitID:self.interstitialId];
        self.interstitial.delegate = (id <GADInterstitialDelegate>)self;

        GADRequest *request = [GADRequest request];

        [self.interstitial loadRequest:request];
    }];
}

- (void)admobRequestRewardVideo {
    [self.commandDelegate runInBackground:^{
        [GADRewardBasedVideoAd sharedInstance].delegate = (id <GADRewardBasedVideoAdDelegate>)self;
        [[GADRewardBasedVideoAd sharedInstance] loadRequest:[GADRequest request] withAdUnitID:self.rewardVideoId];
    }];
}

- (void)admobSetup:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString* appId = [command.arguments objectAtIndex:0];
    NSString* interstitialId = [command.arguments objectAtIndex:1];
    NSString* rewardVideoId = [command.arguments objectAtIndex:2];
    NSArray* testDevices = [command.arguments objectAtIndex:3];

    if (interstitialId == nil) {
        interstitialId = @"ca-app-pub-3940256099942544/4411468910";
    }

    if (rewardVideoId == nil) {
        rewardVideoId = @"ca-app-pub-3940256099942544/1712485313";
    }

    if (testDevices && testDevices.count) {
        GADMobileAds.sharedInstance.requestConfiguration.testDeviceIdentifiers = testDevices;
    }

    self.applicationId = appId;
    self.interstitialId = interstitialId;
    self.rewardVideoId = rewardVideoId;

    [self admobRequestInterstitial];
    [self admobRequestRewardVideo];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)admobShowInterstitial:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [self.commandDelegate runInBackground:^{
        if (self.interstitial.isReady) {
            [self.interstitial presentFromRootViewController:self.viewController];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)admobShowRewardVideo:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [self.commandDelegate runInBackground:^{
        if ([[GADRewardBasedVideoAd sharedInstance] isReady]) {
            [[GADRewardBasedVideoAd sharedInstance] presentFromRootViewController:self.viewController];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)analyticsLogEvent:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString* name = [command.arguments objectAtIndex:0];
    NSDictionary *parameters;

    @try {
        NSString *description = NSLocalizedString([command argumentAtIndex:1 withDefault:@"No Message Provided"], nil);

        parameters = @{ NSLocalizedDescriptionKey: description };
    }
    @catch (NSException *execption) {
        parameters = [command argumentAtIndex:1];
    }

    [self.commandDelegate runInBackground:^{
        [FIRAnalytics logEventWithName:name parameters:parameters];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)analyticsSetUserId:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString* userId = [command.arguments objectAtIndex:0];

    [FIRAnalytics setUserID:userId];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)analyticsSetScreenName:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString* name = [command.arguments objectAtIndex:0];

    [FIRAnalytics setScreenName:name screenClass:NULL];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)analyticsSetUserProperty:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString* name = [command.arguments objectAtIndex:0];
    NSString* value = [command.arguments objectAtIndex:1];

    [FIRAnalytics setUserPropertyString:name forName:value];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)crashlyticsTest:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [self.commandDelegate runInBackground:^{
        [[Crashlytics sharedInstance] crash];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)remoteConfigFetch:(CDVInvokedUrlCommand *)command {
    long expirationDuration = [[command argumentAtIndex:43200] longValue];

    [self.remoteConfig fetchWithExpirationDuration:expirationDuration completionHandler:^(FIRRemoteConfigFetchStatus status, NSError *error) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

        if (error) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)remoteConfigSetup:(CDVInvokedUrlCommand *)command {
    long expirationDuration = [[command argumentAtIndex:43200] longValue];

    FIRRemoteConfigSettings *remoteConfigSettings = [[FIRRemoteConfigSettings alloc] init];

    self.remoteConfig = [FIRRemoteConfig remoteConfig];
    self.remoteConfig.configSettings = remoteConfigSettings;
    self.remoteConfig.configSettings.minimumFetchInterval = expirationDuration;

    [self.remoteConfig fetchAndActivateWithCompletionHandler:^(FIRRemoteConfigFetchAndActivateStatus status, NSError * _Nullable error) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

        if (error) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)remoteConfigGetArray:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        FIRRemoteConfigValue* configValue = [self getConfigValue:command];

        if (configValue != nil) {
            NSError *error = nil;
            NSArray *jsonArray = [NSJSONSerialization JSONObjectWithData:configValue.dataValue options:kNilOptions error:&error];

            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:jsonArray];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)remoteConfigGetBoolean:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        FIRRemoteConfigValue* configValue = [self getConfigValue:command];

        if (configValue != nil) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:configValue.boolValue];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)remoteConfigGetNumber:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        FIRRemoteConfigValue* configValue = [self getConfigValue:command];

        if (configValue != nil) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDouble:[configValue.numberValue doubleValue]];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)remoteConfigGetString:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        FIRRemoteConfigValue* configValue = [self getConfigValue:command];

        if (configValue != nil) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:configValue.stringValue];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (FIRRemoteConfigValue*)getConfigValue:(CDVInvokedUrlCommand *)command {
    NSString* key = [command argumentAtIndex:0];

    return [self.remoteConfig configValueForKey:key];
}

// INTERSTITIALS AD EVENTS
- (void)interstitialDidDismissScreen:(GADInterstitial *)interstitial {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [pluginResult setKeepCallbackAsBool:true];

    [self admobRequestInterstitial];

    [self.commandDelegate runInBackground:^{
        [self.commandDelegate sendPluginResult:pluginResult callbackId:self.interstitialClosedCallbackId];
    }];
}

- (void)onInterstitialClosed:(CDVInvokedUrlCommand *)command {
    self.interstitialClosedCallbackId = command.callbackId;
}


// REWARDED VIDEO AD EVENTS
- (void)rewardBasedVideoAdDidClose:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [pluginResult setKeepCallbackAsBool:true];

    [self admobRequestRewardVideo];

    [self.commandDelegate runInBackground:^{
        [self.commandDelegate sendPluginResult:pluginResult callbackId:self.rewardVideoClosedCallbackId];
    }];
}

- (void)rewardBasedVideoAdDidCompletePlaying:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [pluginResult setKeepCallbackAsBool:true];

    [self.commandDelegate runInBackground:^{
        [self.commandDelegate sendPluginResult:pluginResult callbackId:self.rewardVideoCompleteCallbackId];
    }];
}

- (void)onRewardVideoClosed:(CDVInvokedUrlCommand *)command {
    self.rewardVideoClosedCallbackId = command.callbackId;
}

- (void)onRewardVideoComplete:(CDVInvokedUrlCommand *)command {
    self.rewardVideoCompleteCallbackId = command.callbackId;
}

@end
