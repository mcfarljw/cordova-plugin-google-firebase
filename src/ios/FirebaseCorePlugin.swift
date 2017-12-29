import Firebase

@objc(FirebaseCorePlugin)
class FirebaseCorePlugin : CDVPlugin {

  @objc(pluginInitialize)
  override func pluginInitialize() {
    if (FirebaseApp.app() == nil) {
      FirebaseApp.configure()
    }
  }

}
