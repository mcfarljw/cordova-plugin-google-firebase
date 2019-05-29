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
