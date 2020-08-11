package com.cordova.plugin.jitsi;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PermissionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.content.Intent;
import android.content.Context;
import java.util.Map;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Bundle;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import android.view.View;

import org.apache.cordova.CordovaWebView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import com.facebook.react.modules.core.PermissionListener;

public class JitsiPlugin extends CordovaPlugin implements JitsiMeetActivityInterface{
  private JitsiMeetView view;
  private static final String TAG = "cordova-plugin-jitsi";
  private CallbackContext callbackContext;

  final static String[] permissions = { Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
  public static final int TAKE_PIC_SEC = 0;
  public static final int REC_MIC_SEC = 1;

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    // CB-10120: The CAMERA permission does not need to be requested unless it is declared
    // in AndroidManifest.xml. This plugin does not declare it, but others may and so we must
    // check the package info to determine if the permission is present.

    checkPermission();

    if (action.equals("loadURL")) {
      String url = args.getString(0);
      String key = args.getString(1);
      String userDisplayName = args.getString(2);
      this.loadURL(url, key, userDisplayName, callbackContext);
      return true;
    } else if (action.equals("destroy")) {
      this.destroy(callbackContext);
      return true;
    }
    return false;
  }


  private void checkPermission(){
    boolean takePicturePermission = PermissionHelper.hasPermission(this, Manifest.permission.CAMERA);
    boolean micPermission = PermissionHelper.hasPermission(this, Manifest.permission.RECORD_AUDIO);

    // CB-10120: The CAMERA permission does not need to be requested unless it is declared
    // in AndroidManifest.xml. This plugin does not declare it, but others may and so we must
    // check the package info to determine if the permission is present.

    Log.e(TAG, "tp : " + takePicturePermission);
    Log.e(TAG, "mp : " + micPermission);

    if (!takePicturePermission) {
      takePicturePermission = true;

      try {
        PackageManager packageManager = this.cordova.getActivity().getPackageManager();
        String[] permissionsInPackage = packageManager.getPackageInfo(this.cordova.getActivity().getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;

        if (permissionsInPackage != null) {
          for (String permission : permissionsInPackage) {
            if (permission.equals(Manifest.permission.CAMERA)) {
              takePicturePermission = false;
              break;
            }
          }
        }
        Log.e(TAG, "10 : ");
      } catch (NameNotFoundException e) {
        // We are requesting the info for our package, so this should
        // never be caught
        Log.e(TAG, e.getMessage());
      }
    }

    if(!takePicturePermission){
      PermissionHelper.requestPermissions(this, TAKE_PIC_SEC,
        new String[]{ Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});
    }
  }

  private void loadURL(final String url, final String key, final String userDisplayName, final CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
        Log.e(TAG, "loadURL called : "+url);
        Context context = cordova.getActivity();
        JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();
        userInfo.setDisplayName(userDisplayName);
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
              .setRoom(url)
              .setSubject("EAGLE ROOM - " + key)
              .setUserInfo(userInfo)
              // .setFeatureFlag("chat.enabled", false)
              // .setFeatureFlag("invite.enabled", false)
              // .setFeatureFlag("calendar.enabled", false)
              .setVideoMuted(true)
              .setWelcomePageEnabled(false)
              .build();
        Log.e(TAG, "SREENADH called myJitsiActivity ");

        // myJitsiMeetActivity.launch(cordova.getActivity(), options);

        String ACTION_JITSI_MEET_CONFERENCE = "org.jitsi.meet.CONFERENCE";
        String JITSI_MEET_CONFERENCE_OPTIONS = "JitsiMeetConferenceOptions";
        Intent intent = new Intent(cordova.getActivity(), srisysJitsiMeetActivity.class);
        intent.setAction(ACTION_JITSI_MEET_CONFERENCE);
        intent.putExtra(JITSI_MEET_CONFERENCE_OPTIONS, options);
        cordova.setActivityResultCallback(this);
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "CONFERENCE_INITIATED");
        pluginResult.setKeepCallback(true);
        this.callbackContext.sendPluginResult(pluginResult);
        cordova.getActivity().startActivityForResult(intent,2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "SREENADH called onActivityResult ");
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "CONFERENCE_TERMINATED");
        pluginResult.setKeepCallback(false);
        this.callbackContext.sendPluginResult(pluginResult);

        super.onActivityResult(requestCode, resultCode, data);
    }

  private void destroy(final CallbackContext callbackContext) {
    cordova.getActivity().runOnUiThread(new Runnable() {
      public void run() {
        //view.dispose();
        //view = null;
        //JitsiMeetView.onHostDestroy(cordova.getActivity());
        JitsiMeetActivityDelegate.onHostDestroy(cordova.getActivity());
        cordova.getActivity().setContentView(getView());
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "DESTROYED");
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
      }
    });
  }

  private View getView() {
    try {
      return (View) webView.getClass().getMethod("getView").invoke(webView);
    } catch (Exception e) {
      return (View) webView;
    }
  }
  
   @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  
   @Override
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        JitsiMeetActivityDelegate.requestPermissions(cordova.getActivity(), permissions, requestCode, listener);
    }
  
    @Override
    public boolean shouldShowRequestPermissionRationale(String permissions) {
        return true;
    }
  
    @Override
    public int checkSelfPermission(String permission) {
        return 0;
    }
    
    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return 0;
    }
  
}
