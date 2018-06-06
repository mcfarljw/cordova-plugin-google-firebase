const filesystem = require('fs-extra')
const path = require('path')
const utils = require('../utils')

module.exports = function (context) {
  const projectRoot = path.resolve(context.opts.projectRoot)
  const platformRoot = path.resolve(projectRoot, 'platforms/android')
  const gradleSource1 = path.resolve(platformRoot, 'app/build.gradle')
  const gradleSource2 = path.resolve(platformRoot, 'build.gradle')
  let gradleFile
  let gradlePath

  // find gradle build to apply plugins
  if (filesystem.existsSync(gradleSource1)) {
    gradlePath = gradleSource1
    gradleFile = filesystem.readFileSync(gradleSource1, 'utf8')
  } else if (filesystem.existsSync(gradleSource2)) {
    gradlePath = gradleSource2
    gradleFile = filesystem.readFileSync(gradleSource2, 'utf8')
  } else {
    gradleFile = null
  }

  if (gradleFile) {
    // update gradle tools buildscript dependency
    const gradleBuildToolsRegex = new RegExp(/com.android.tools.build:gradle:[0-9]+.[0-9]+.[0-9]+/)
    const gradleBuildToolsIndex = utils.getLineIndex(gradleFile, gradleBuildToolsRegex)
    if (gradleBuildToolsIndex > -1) {
      gradleFile = gradleFile.replace(gradleBuildToolsRegex, 'com.android.tools.build:gradle:3.0.1')
    }

    // insert or update google services buildscript dependency
    const googleServicesRegex = new RegExp(/com.google.gms:google-services:[0-9]+.[0-9]+.[0-9]+/)
    const googleServicesIndex = utils.getLineIndex(gradleFile, googleServicesRegex)

    if (googleServicesIndex > -1) {
      gradleFile = gradleFile.replace(googleServicesRegex, 'com.google.gms:google-services:4.0.1')
    } else {
      gradleFile = utils.insertLineAt(gradleFile, gradleBuildToolsIndex + 1, "\t\tclasspath 'com.google.gms:google-services:4.0.1'")
    }

    // apply google services plugin if missing
    const googleServicesApplyIndex = utils.getLineIndex(gradleFile, "apply plugin: 'com.google.gms.google-services'")
    if (googleServicesApplyIndex === -1) {
      gradleFile = utils.appendLine(gradleFile, "apply plugin: 'com.google.gms.google-services'")
    }

    filesystem.writeFileSync(gradlePath, gradleFile, 'utf8')
  }
}
