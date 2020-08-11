package com.cordova.plugin.jitsi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.react.modules.core.PermissionListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class srisysJitsiMeetActivity extends JitsiMeetActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          Log.e(TAG, "SREENADH called onCreate ");
          super.onCreate(savedInstanceState);
      }
      @Override
      public void onConferenceJoined(Map<String, Object> data) {
          Log.e(TAG, "SREENADH called onConferenceJoined ");
          super.onConferenceJoined(data);
      }
      @Override
      public void onConferenceTerminated(Map<String, Object> data) {
          Log.e(TAG, "SREENADH called onConferenceTerminated ");
          Intent intent=new Intent();
          intent.putExtra("MESSAGE","CONFERENCE TERMINATED");
          setResult(2,intent);

          super.onConferenceTerminated(data);
      }
      @Override
      public void onConferenceWillJoin(Map<String, Object> data) {
          Log.e(TAG, "SREENADH called onConferenceWillJoin ");
          super.onConferenceWillJoin(data);
      }
}