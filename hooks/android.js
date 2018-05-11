const filesystem = require('fs-extra')
const path = require('path')

module.exports = function (context) {
  const cordovaDirectory = path.resolve(context.opts.projectRoot)
  const googleServicesSource1 = path.resolve(cordovaDirectory, 'google-services.json')
  const googleServicesSource2 = path.resolve(cordovaDirectory, '../', 'google-services.json')
  const googleServicesTargetDirectory = path.resolve(cordovaDirectory, 'platforms/android')
  const googleServicesTarget = path.resolve(googleServicesTargetDirectory, 'google-services.json')

  // ensure root directory exists
  filesystem.ensureDirSync(googleServicesTargetDirectory)

  // find and copy google services to root directory
  if (filesystem.existsSync(googleServicesSource1)) {
    filesystem.writeFileSync(googleServicesTarget, filesystem.readFileSync(googleServicesSource1))
  } else if (filesystem.existsSync(googleServicesSource2)) {
    filesystem.writeFileSync(googleServicesTarget, filesystem.readFileSync(googleServicesSource2))
  }
}
