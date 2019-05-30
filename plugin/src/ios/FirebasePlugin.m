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

        request.testDevices = self.testDevices;

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

    if (testDevices && testDevices.count) {
        [self.testDevices addObjectsFromArray:testDevices];
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

    [self.commandDelegate runInBackground:^{
        [FIRAnalytics setUserID:userId];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)analyticsSetScreenName:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString* name = [command.arguments objectAtIndex:0];

    [self.commandDelegate runInBackground:^{
        [FIRAnalytics setScreenName:name screenClass:NULL];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)analyticsSetUserProperty:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString* name = [command.arguments objectAtIndex:0];
    NSString* value = [command.arguments objectAtIndex:1];

    [self.commandDelegate runInBackground:^{
        [FIRAnalytics setUserPropertyString:name forName:value];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)crashlyticsTest:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [self.commandDelegate runInBackground:^{
        [[Crashlytics sharedInstance] crash];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)remoteConfigSetup:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [self.commandDelegate runInBackground:^{
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
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
    self.interstitialClosedCallbackId = command.callbackId
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
