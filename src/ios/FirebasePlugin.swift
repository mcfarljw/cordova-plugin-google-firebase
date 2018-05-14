import Firebase

@objc(FirebasePlugin)
class FirebasePlugin : CDVPlugin {

  @objc(pluginInitialize)
  override func pluginInitialize() {
    if (FirebaseApp.app() == nil) {
      FirebaseApp.configure()
    }
  }

  @objc(logEvent:)
  func analyticsLogEvent(command: CDVInvokedUrlCommand) {
    DispatchQueue.global(qos: .userInitiated).async {
      let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
      let eventName = command.arguments[0] as? String ?? ""
      let eventParameters = command.arguments[1] as? [String: Any] ?? nil

      Analytics.logEvent(eventName, parameters: eventParameters)

      self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
  }

  @objc(setUserId:)
  func analyticsSetUserId(command: CDVInvokedUrlCommand) {
    DispatchQueue.global(qos: .userInitiated).async {
      let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
      let userId = command.arguments[0] as? String ?? ""

      Analytics.setUserID(userId)

      self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
  }

}
