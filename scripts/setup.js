#!/usr/bin/env node

const { emptyDirSync } = require('fs-extra')
const { resolve } = require('path')
const shell = require('shelljs')
const root = resolve(__dirname, '..')

// clean directory for fresh install
emptyDirSync(`${root}/test/platforms`)
emptyDirSync(`${root}/test/plugins`)

// navigate to cordova directory
shell.cd(`${root}/test`)

// add platforms
shell.exec('cordova platform add android@8.0.0')
shell.exec('cordova platform add ios@5.0.1')

// add plugins
shell.exec(`cordova plugin add ${root}/plugin --variable APP_ID="ca-app-pub-3940256099942544~3347511713"`)
shell.exec('cordova plugin add cordova-plugin-device@2.0.2')
shell.exec('cordova plugin add cordova-plugin-ionic-webview@4.0.1')
