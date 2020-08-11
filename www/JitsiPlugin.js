var exec = require('cordova/exec');

exports.loadURL = function(url, key, userDisplayName, isInvisible, success, error) {
    exec(success, error, "JitsiPlugin", "loadURL", [url, key, userDisplayName, !!isInvisible]);
};

exports.destroy = function(success, error) {
    exec(success, error, "JitsiPlugin", "destroy", []);
};