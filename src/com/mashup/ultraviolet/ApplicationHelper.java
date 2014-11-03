package com.mashup.ultraviolet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

public class ApplicationHelper {


  public static void releaseImageView(ImageView imageView){
    if (imageView != null) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable)(imageView.getDrawable());
      if (bitmapDrawable != null) {
        bitmapDrawable.setCallback(null);
      }
      imageView.setImageBitmap(null);
    }
  }

  //---------------------------------------------------------------------------------------------------------------------------------------------------------------

  public static String makeUrlParams(Bundle params){
    Set<String> keys = params.keySet();
    ArrayList<String> paramList = new ArrayList<String>();
    for (String key : keys) {
      paramList.add(key + "=" + params.get(key).toString());
    }
    return ApplicationHelper.join(paramList, "&");
  }

  //---------------------------------------------------------------------------------------------------------------------------------------------------------------

  public static String makeUrlParams(Map<String, Object> params){
    Set<String> keys = params.keySet();
    ArrayList<String> paramList = new ArrayList<String>();
    for(Entry<String, Object> e : params.entrySet()) {
      paramList.add(e.getKey() + "=" + e.getValue().toString());
    }
    return ApplicationHelper.join(paramList, "&");
  }

  //---------------------------------------------------------------------------------------------------------------------------------------------------------------

  public static String join(String[] list, String with) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < list.length; i++) {
    if (i != 0) { buf.append(with);}
      buf.append(list[i]);
    }
    return buf.toString();
  }

  //---------------------------------------------------------------------------------------------------------------------------------------------------------------

  public static String join(ArrayList<String> list, String with) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < list.size(); i++) {
      if (i != 0) { buf.append(with);}
      buf.append(list.get(i));
    }
    return buf.toString();
  }

  //ネットワークに接続されているかどうかの判別
  public static boolean checkNetWork(Context context){
    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    if(networkInfo != null){
      return connectivityManager.getActiveNetworkInfo().isConnected();
    }
    return false;
  }

  //---------------------------------------------------------------------------------------------------------------------------------------------------------------

  //Toastの表示
  public static void showToast(Context con, String message) {
    Toast toast = Toast.makeText(con, message, Toast.LENGTH_LONG);
    toast.show();
  }
}
