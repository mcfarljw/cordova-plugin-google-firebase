import Crashlytics
import Firebase
import GoogleMobileAds

var testDevices: [String] = []

@objc(FirebasePlugin)
class FirebasePlugin : CDVPlugin {

    var applicationId: String = "ca-app-pub-3940256099942544~1458002511"
    var interstitial: GADInterstitial!
    var interstitialUnitId: String = "ca-app-pub-3940256099942544/4411468910"


    @objc(pluginInitialize)
    override func pluginInitialize() {
        if (FirebaseApp.app() == nil) {
            FirebaseApp.configure()
        }
    }

    @objc(admobAddTestDevice:)
    func admobAddTestDevice(command: CDVInvokedUrlCommand) {
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        let testDeviceId = command.arguments[0] as? String ?? ""

        if !testDeviceId.isEmpty {
            testDevices.append(testDeviceId)
        }

        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }

    @objc(admobGetTestDevices:)
    func admobGetTestDevices(command: CDVInvokedUrlCommand) {
        self.commandDelegate.send(
            CDVPluginResult(status: CDVCommandStatus_OK, messageAs: testDevices),
            callbackId: command.callbackId
        )
    }

    @objc(admobRequestInterstitial)
    func admobRequestInterstitial() {
        DispatchQueue.global(qos: .userInitiated).async {
            self.interstitial = GADInterstitial(adUnitID: self.interstitialUnitId)

            let request = GADRequest()

            request.testDevices = testDevices

            self.interstitial.load(request)
        }
    }

    @objc(admobSetAdmobAppId:)
    func admobSetAdmobAppId(command: CDVInvokedUrlCommand) {
        DispatchQueue.global(qos: .userInitiated).async {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)

            self.applicationId = command.arguments[0] as? String ?? "ca-app-pub-3940256099942544~1458002511"

            DispatchQueue.main.async {
                GADMobileAds.configure(withApplicationID: self.applicationId)

                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            }
        }
    }

    @objc(admobSetInterstitialId:)
    func admobSetInterstitialId(command: CDVInvokedUrlCommand) {
        DispatchQueue.global(qos: .userInitiated).async {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)

            self.interstitialUnitId = command.arguments[0] as? String ?? "ca-app-pub-3940256099942544/4411468910"

            self.admobRequestInterstitial()

            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }
    }

    @objc(admobShowInterstitial:)
    func admobShowInterstitial(command: CDVInvokedUrlCommand) {
        DispatchQueue.global(qos: .userInitiated).async {
            var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)

            DispatchQueue.main.async {
                if self.interstitial != nil && self.interstitial.isReady {
                    self.interstitial.present(fromRootViewController: self.viewController)
                } else {
                    pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
                }

                self.admobRequestInterstitial()

                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            }
        }
    }

    @objc(analyticsLogEvent:)
    func analyticsLogEvent(command: CDVInvokedUrlCommand) {
        DispatchQueue.global(qos: .userInitiated).async {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
            let eventName = command.arguments[0] as? String ?? ""
            let eventParameters = command.arguments[1] as? [String: Any] ?? nil

            Analytics.logEvent(eventName, parameters: eventParameters)

            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }
    }

    @objc(analyticsSetUserId:)
    func analyticsSetUserId(command: CDVInvokedUrlCommand) {
        DispatchQueue.global(qos: .userInitiated).async {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
            let userId = command.arguments[0] as? String ?? ""

            Analytics.setUserID(userId)

            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }
    }

    @objc(crashlyticsTest:)
    func crashlyticsTest(command: CDVInvokedUrlCommand) {
        DispatchQueue.global(qos: .userInitiated).async {
            Crashlytics.sharedInstance().crash()
        }
    }

}
