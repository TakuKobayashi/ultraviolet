package com.mashup.ultraviolet;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashup.ultraviolet.HttpRequestTask.RequestFinishCallback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

  private RelativeLayout _tipsLayout;
  private TextView _todayValText;
  private TextView _tomorrowValText;
  private LocationManager _locationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(ExtraLayout.getParenetView(this, R.layout.activity_main));
    ImageView bg = (ImageView) findViewById(R.id.SkyBg);
    ExtraLayout.setBaseImageView(this, bg, R.drawable.sky_bg);

    _tipsLayout = (RelativeLayout) findViewById(R.id.UVInfoLayout);
    _tipsLayout.getLayoutParams().width = bg.getLayoutParams().width;
    _todayValText = (TextView) findViewById(R.id.TodayValText);
    _todayValText.setText(R.string.loadding);
    _tomorrowValText = (TextView) findViewById(R.id.TommorowValText);
    _tomorrowValText.setText(R.string.loadding);
    _locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
  }

  private void httpLocationsRequest(double lat, double lon){
    HttpGetRequestTask request = new HttpGetRequestTask(new RequestFinishCallback() {
      @Override
        public void serverError(int statusCode, String message, HttpResponse response) {
        }

        @Override
        public void complete(String result) {
          try {
            JSONObject json = new JSONObject(result);
            double todayVal = json.optDouble("today_val");
            double tomorrowVal = json.optDouble("tomorrow_val");
            _tipsLayout.setBackgroundColor(valToColor(todayVal));
            _todayValText.setText(String.valueOf(todayVal));
            _tomorrowValText.setText(String.valueOf(tomorrowVal));
           } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void clientError(Exception e) {

        }
      });
      Bundle params = new Bundle();
      params.putDouble("lat", lat);
      params.putDouble("lon", lon);
      request.execute("http://taptappun.cloudapp.net/api/environment/ulterviolet?"+ApplicationHelper.makeUrlParams(params));
  }

  private int valToColor(double val){
    if(val >= 11){
      return Color.argb(192, 255, 153, 255);
    }else if(val >= 8){
      return Color.argb(192, 255, 102, 102);
    }else if(val >= 6){
      return Color.argb(192, 255, 204, 102);
    }else if(val >= 3){
      return Color.argb(192, 255, 255, 154);
    }else{
      return Color.argb(192, 154, 255, 154);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    Location location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if(location == null){
      location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }
    if(location != null){
      httpLocationsRequest(location.getLatitude(), location.getLongitude());
    }
    _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5000, _locationListener); // 位置情報リスナー
    _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5000, _locationListener); // 位置情報リスナー
  }

  @Override
  protected void onPause() {
    super.onPause();
    _locationManager.removeUpdates(_locationListener);
  }

  private LocationListener _locationListener = new LocationListener() {
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onLocationChanged(Location location) {
      httpLocationsRequest(location.getLatitude(), location.getLongitude());
    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ApplicationHelper.releaseImageView((ImageView) findViewById(R.id.SkyBg));
  }
}
