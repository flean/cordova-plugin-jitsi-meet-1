# cordova-plugin-jitsi-meet
Cordova plugin for Jitsi Meet React Native SDK. Works with both iOS and Android, and fixes the 64-bit binary dependency issue with Android found in previous versions of this plugin.

# Summary 
I created this repo as there were multiple versions of this plugin, one which worked for iOS and Android, but neither worked with both. This verison satisfies the 64bit requirement for Android and also works in iOS and is currently being used in production.

# Installation
`cordova plugin add https://github.com/findmate/cordova-plugin-jitsi-meet`

# Usage
```
const roomId = 'your-custom-room-id';

jitsiplugin.loadURL('https://meet.jit.si/' + roomId, roomId, false, function (data) {
    if (data === "CONFERENCE_WILL_LEAVE") {
        jitsiplugin.destroy(function (data) {
            // call finished
        }, function (err) {
            console.log(err);
        });
    }
}, function (err) {
    console.log(err);
});
```

# Credits
* Big thanks to @peixoto2000 for helping figure out how to embed the frameworks into the project automatically
