import Firebase

@objc(FirebasePlugin)
class FirebasePlugin : CDVPlugin {
    var dynamicLinkCallback = ""
    var remoteConfig: RemoteConfig!

    override func pluginInitialize() {
        DynamicLinks.performDiagnostics(completion: nil)
    }

    @objc(analyticsLogEvent:)
    private func analyticsLogEvent(command: CDVInvokedUrlCommand) {
        let name = command.arguments[0] as? String ?? ""
        let parameters = command.arguments[1] as? [String : Any]

        Analytics.logEvent(name, parameters: parameters)

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK), callbackId: command.callbackId)
    }

    @objc(analyticsSetScreenName:)
    private func analyticsSetScreenName(command: CDVInvokedUrlCommand) {
        let name = command.arguments[0] as? String ?? ""

        Analytics.logEvent(AnalyticsEventScreenView, parameters: [AnalyticsParameterScreenName: name])

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK), callbackId: command.callbackId)
    }

    @objc(analyticsSetUserId:)
    private func analyticsSetUserId(command: CDVInvokedUrlCommand) {
        let userId = command.arguments[0] as? String ?? ""

        Analytics.setUserID(userId)

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK), callbackId: command.callbackId)
    }

    @objc(analyticsSetUserProperty:)
    private func analyticsSetUserProperty(command: CDVInvokedUrlCommand) {
        let name = command.arguments[0] as? String ?? ""
        let value = command.arguments[0] as? String ?? ""

        Analytics.setUserProperty(name, forName: value)

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK), callbackId: command.callbackId)
    }

    @objc(dynamicLinksData:)
    private func dynamicLinksData(command: CDVInvokedUrlCommand) {
        dynamicLinkCallback = command.callbackId
    }

    @objc(remoteConfigFetch:)
    private func remoteConfigFetch(command: CDVInvokedUrlCommand) {
        remoteConfig.fetch() { (status, error) -> Void in
          if status == .success {
            self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK), callbackId: command.callbackId)
          } else {
            self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_ERROR), callbackId: command.callbackId)
          }
        }
    }

    @objc(remoteConfigSetup:)
    private func remoteConfigSetup(command: CDVInvokedUrlCommand) {
        let interval = command.arguments[0] as? Double ?? 43200
        let settings = RemoteConfigSettings()

        settings.minimumFetchInterval = interval

        remoteConfig = RemoteConfig.remoteConfig()
        remoteConfig.configSettings = settings
        remoteConfig.fetch() { (status, error) -> Void in
          if status == .success {
            self.remoteConfig.activate() { (changed, error) in
                self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK), callbackId: command.callbackId)
            }
          } else {
            self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_ERROR), callbackId: command.callbackId)
          }
        }
    }

    @objc(remoteConfigGetArray:)
    private func remoteConfigGetArray(command: CDVInvokedUrlCommand) {
        let name = command.arguments[0] as? String ?? ""
        var value: [AnyHashable] = []

        do {
            value = try JSONSerialization.jsonObject(with: remoteConfig.configValue(forKey: name).dataValue, options: []) as? [AnyHashable] ?? []
        } catch {
            value = []
        }

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK, messageAs: value), callbackId: command.callbackId)
    }

    @objc(remoteConfigGetBoolean:)
    private func remoteConfigGetBoolean(command: CDVInvokedUrlCommand) {
        let name = command.arguments[0] as? String ?? ""
        let value = remoteConfig.configValue(forKey: name).boolValue

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK, messageAs: value), callbackId: command.callbackId)
    }

    @objc(remoteConfigGetNumber:)
    private func remoteConfigGetNumber(command: CDVInvokedUrlCommand) {
        let name = command.arguments[0] as? String ?? ""
        let value = remoteConfig.configValue(forKey: name).numberValue as! Double

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK, messageAs: value), callbackId: command.callbackId)
    }

    @objc(remoteConfigGetString:)
    private func remoteConfigGetString(command: CDVInvokedUrlCommand) {
        let name = command.arguments[0] as? String ?? ""
        let value = remoteConfig.configValue(forKey: name).stringValue

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK, messageAs: value), callbackId: command.callbackId)
    }

    func application(_ application: UIApplication, continue userActivity: NSUserActivity,
                     restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
      let handled = DynamicLinks.dynamicLinks().handleUniversalLink(userActivity.webpageURL!) { (dynamiclink, error) in
        let url = dynamiclink?.url ?? ""

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK, messageAs: url), callbackId: command.callbackId)
      }

      return handled
    }

    @available(iOS 9.0, *)
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any]) -> Bool {
      return application(app, open: url,
                         sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String,
                         annotation: "")
    }

    func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
      if let dynamicLink = DynamicLinks.dynamicLinks().dynamicLink(fromCustomSchemeURL: url) {
        let url = dynamicLink.url ?? ""

        self.commandDelegate.send(CDVPluginResult(status: CDVCommandStatus_OK, messageAs: url), callbackId: command.callbackId)

        return true
      }

      return false
    }
}
