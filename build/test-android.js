#!/usr/bin/env node

const { ensureDirSync, emptyDirSync } = require('fs-extra')
const { resolve } = require('path')
const shell = require('shelljs')
const root = resolve(__dirname, '..')

// ensure cordova directory exists and is empty
ensureDirSync(`${root}/test`)
emptyDirSync(`${root}/test`)

// install cordova
shell.exec(`cordova create ${root}/test com.jernung.testing "Firebase"`)
shell.cd(`${root}/test`)

// add platforms
shell.exec('cordova platform add android')

// add plugins
shell.exec('cordova plugin add ..')
