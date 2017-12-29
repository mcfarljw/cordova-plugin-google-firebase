import Firebase

@objc(FirebaseCorePlugin)
class FirebaseCorePlugin : CDVPlugin {

  @objc(pluginInitialize)
  func pluginInitialize() {
    if (FirebaseApp.app() == nil) {
      FirebaseApp.configure()
    }
  }

}
