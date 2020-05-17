#!/usr/bin/env node

const { resolve } = require('path')
const shell = require('shelljs')
const root = resolve(__dirname, '..')

// navigate to cordova directory
shell.cd(`${root}/test`)

// run on device on ios
shell.exec('cordova build ios --buildConfig')
