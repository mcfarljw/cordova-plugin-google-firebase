import Firebase

@objc(FirebasePlugin)
class FirebasePlugin : CDVPlugin {

  @objc(pluginInitialize)
  override func pluginInitialize() {
    if (FirebaseApp.app() == nil) {
      FirebaseApp.configure()
    }
  }

}
