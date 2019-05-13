const filesystem = require('fs-extra')
const convert = require('xml-js')

module.exports = function (context) {
  const root = context.opts.projectRoot
  const config = convert.xml2js(filesystem.readFileSync(`${root}/config.xml`).toString(), { compact: true })
  const name = config.widget.name._text

  return {
    ANDROID_GOOGLE_SERVICES: `${root}/platforms/android/app`,
    ANDROID_PLATFORM: `${root}/platforms/android`,
    IOS_GOOGLE_SERVICES: `${root}/platforms/ios/${name}/Resources`,
    IOS_PLATFORM: `${root}/platforms/ios/${name}`,
    IOS_XCODEPROJ: `${root}/platforms/ios/${name}.xcodeproj`,
    NAME: name,
    ROOT: root
  }
}
