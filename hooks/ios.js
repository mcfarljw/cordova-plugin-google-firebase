const filesystem = require('fs-extra')
const path = require('path')
const ConfigParser = require('cordova-common').ConfigParser

module.exports = function (context) {
  const projectRoot = context.opts.projectRoot;
  const config = new ConfigParser(path.resolve(projectRoot, 'config.xml'))

  const googleServicesSourceFile1 = path.resolve(projectRoot, 'GoogleService-Info.plist')
  const googleServicesSourceFile2 = path.resolve(projectRoot, '../', 'GoogleService-Info.plist')
  const googleServicesTargetDirectory = path.resolve(projectRoot, 'platforms/ios', config.name(), 'Resources')
  const googleServicesTargetFile = path.resolve(googleServicesTargetDirectory, 'GoogleService-Info.plist')

  // ensure resources directory exists
  filesystem.ensureDirSync(googleServicesTargetDirectory)

  // find and copy google services to resources directory
  if (filesystem.existsSync(googleServicesSourceFile1)) {
    filesystem.copyFileSync(googleServicesSourceFile1, googleServicesTargetFile)
  } else if (filesystem.existsSync(googleServicesSourceFile2)) {
    filesystem.copyFileSync(googleServicesSourceFile2, googleServicesTargetFile)
  }
}
