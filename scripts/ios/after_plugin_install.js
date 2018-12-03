const filesystem = require('fs-extra')
const path = require('path')
const xcode = require('xcode')

module.exports = async function (context) {
  const globals = require('../globals')(context)
  // const utils = require('../utils')(context)
  const project = xcode.project(`${globals.IOS_XCODEPROJ}/project.pbxproj`)

  project.parseSync()

  async function setupCrashlytics () {
    const key = project.generateUuid()
    const comment = '\"Crashlytics\"'

    project.hash.project.objects.PBXShellScriptBuildPhase[key] = {
      isa: 'PBXShellScriptBuildPhase',
      buildActionMask: 2147483647,
      files: [],
      inputFileListPaths: [],
      inputPaths: [
        '\"$(BUILT_PRODUCTS_DIR)/$(INFOPLIST_PATH)\"'
      ],
      outputFileListPaths: [],
      outputPaths: [],
      runOnlyForDeploymentPostprocessing: 0,
      shellPath: '/bin/sh',
      shellScript: '\"${PROJECT_DIR}' + `/${globals.NAME}/Plugins/${context.opts.plugin.id}/Fabric.framework/run\"`
    }

    project.hash.project.objects.PBXShellScriptBuildPhase[`${key}_comment`] = comment

    for (let nativeTargetId in project.hash.project.objects.PBXNativeTarget) {

      if (nativeTargetId.indexOf('_comment') !== -1) {
        continue
      }

      let nativeTarget = project.hash.project.objects.PBXNativeTarget[nativeTargetId]

      nativeTarget.buildPhases.push({
        value: key,
        comment: comment
      })
    }

    filesystem.writeFileSync(`${globals.IOS_XCODEPROJ}/project.pbxproj`, project.writeSync());
  }

  await setupCrashlytics()
}
