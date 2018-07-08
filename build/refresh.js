#!/usr/bin/env node

const shell = require('shelljs')

shell.cd('../test')
shell.exec('cordova plugin remove cordova-plugin-google-firebase')
shell.exec('cordova plugin add ../')
