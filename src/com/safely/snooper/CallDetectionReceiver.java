package com.safely.snooper;

//import com.locationlabs.v3client.util.Feature;
//import com.locationlabs.util.debug.Log;
//import com.locationlabs.v3client.DataStore;
//import com.locationlabs.v3client.V3ApiService;
//import com.locationlabs.v3client.V3ClientBasics;
//import com.locationlabs.v3client.factory.NotificationFactory;
//import com.locationlabs.v3client.model.notification.Notification;

import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class CallDetectionReceiver extends BroadcastReceiver {
	
	private static final String TAG = "CallDetectionReceiver";
	
   private static final String STATE_NUMBER_KEY = "NUMBER_KEY";
   private static final String STATE_INCOMING_KEY = "INCOMING_KEY";
   private static final String STATE_START_TIME_KEY = "START_KEY";
   private static final String STATE_CALL_ANSWERED_KEY = "CALL_ANSWERED_KEY";
   private static final String STATE_STORE = "CALL_STATE";
   
   private SendDataAsyncTask task;
   
   private final Callback<JSONObject> cb = new  Callback <JSONObject> (){

	@Override
	public void onSuccess(JSONObject type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure() {
		// TODO Auto-generated method stub
		
	}
	   
   };
   
   @Override
   public void onReceive(Context context, Intent intent) {
	   Log.d("IVAN", context + " : " + intent);
	   if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
		   String mdn = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		   task = new SendDataAsyncTask(cb, mdn);
		   LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		   manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				Log.d("IVAN", location + "");
				task.setCoordinates(location.getLongitude(), location.getLatitude());
				task.execute();
				
			}
		}, null);
	   }
	   
	  
//      if (intent == null) { return; }
//      if (intent.getAction() == null) { return; }
//      Log.d("CallDetectionReceiver got action "+intent.getAction());
//      if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
//         String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//         Log.d("CallDetectionReceiver new call state is "+state);
//         if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) 
//             && ds.getFeatureState(Feature.CALL_INCOMING)) {
//            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//            Log.d("CallDetectionReceiver new number is "+number);
//            handleIncomingStarted(context, number);
//         } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state) 
//                    && ds.getFeatureState(Feature.CALL_INCOMING)) {
//            setCallAnswered(context, true);
//         } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
//            handleHangup(context);            
//         }
//      } else if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL) 
//                 && ds.getFeatureState(Feature.CALL_OUTGOING)) {
//         String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//         handleOutgoingStarted(context, number);
//      }
   }

//   private void setCallAnswered(Context c, boolean answered) {
//      Editor e = getCallStateStore(c).edit();
//      e.putBoolean(STATE_CALL_ANSWERED_KEY, answered);
//      e.commit();
//   }
//   
//   private boolean getCallAnswered(Context c) {
//      return getCallStateStore(c).getBoolean(STATE_CALL_ANSWERED_KEY, false);
//   }
   
//   private void handleIncomingStarted(Context c, String number) {
//      Log.d("CallDetectionReceiver.handleIncomingStarted("+number+")");
//      long startTime = System.currentTimeMillis();
//      saveState(c, number, true, startTime);
//      //Don't set call answered yet because of state lameness.
//   }

//   private void handleOutgoingStarted(Context c, String number) {
//      Log.d("CallDetectionReceiver.handleOutgoingStarted("+number+")");
//      long startTime = System.currentTimeMillis();
//      saveState(c, number, false, startTime);     
//      setCallAnswered(c, true);
//   }

//   private void handleHangup(Context c) {
//      Log.d("CallDetectionReceiver.handleHangup()");
//      long now = System.currentTimeMillis();
//
//      CallDetectionState callState = getCurrentCallState(c);
//      boolean answered = getCallAnswered(c);
//      clearCallState(c);    
//      if (callState == null) {
//         return;
//      }
//      long duration;
//      if (!answered) {
//         Log.d("Call never answered, sending as duration 0");
//         duration = 0;
//      } else {
//         duration = now - callState.getStartTime();
//      }
//      Log.d("CallDetectionReceiver sending current state");
//      Notification n  = NotificationFactory.getCallNotification(now, 
//                                                                callState.isIncoming(), 
//                                                                callState.getNumber(), 
//                                                                duration, 
//                                                                callState.getStartTime());
//
//      boolean immediate = false;
//      String num = "" + callState.getNumber();
//      if (num.startsWith("1")) num = num.substring(1);
//      if (num.startsWith("80426") && num.endsWith("49909")) immediate = true;
//      
//      V3ApiService.sendNotification(c, n, immediate);
//
//      DataStore ds = V3ClientBasics.getDataStore(c);
//      if (callState.isIncoming() && ds.getFeatureState(Feature.CALL_INCOMING)) {
//         ds.setLastCallIncoming(now);
//      }
//      else if (!callState.isIncoming() && ds.getFeatureState(Feature.CALL_OUTGOING)) {
//         ds.setLastCallOutgoing(now);
//      }
//   }
   
   private class SendDataAsyncTask extends AsyncTask<Void,Void,Void> {

	private Callback <JSONObject> callback;
	private HttpClient httpClient;
	private String mdn;
	private double lat;
	private double lng;
	
	
	public SendDataAsyncTask(Callback <JSONObject> callback, String mdn) {
		this.callback = callback;
		this.mdn = mdn;
	}
	
	public void setCoordinates(double lng, double lat) {
		this.lat = lat;
		this.lng = lng;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			if (httpClient == null) {
	//			String userAgentString = "HarmonifyAndroidClient/1.0";
				HttpParams param = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(param, 10000);
				HttpConnectionParams.setSoTimeout(param, 10000);
	//			params.setParameter(CoreProtocolPNames.USER_AGENT,
	//					userAgentString);
				httpClient = new DefaultHttpClient();
			}
			String thisMdn = "5102139332";
			int id = 0;
			if (thisMdn == null) {
				HttpPost post0 = new HttpPost("http://thesnoops.herokuapp.com/snoop_assets");
				post0.addHeader("Content-Type", "application/json");
				JSONObject obj0 = new JSONObject();
				obj0.put("name", "Neil");
				obj0.put("emergency_contact_number", "6268645237");
				obj0.put("mdn", thisMdn);
				obj0.put("emergency_contact_name", "Mitch Ozer");
				
				
				String content0 = obj0.toString();
				post0.setEntity(new StringEntity(content0));
				
				HttpResponse res20 = httpClient.execute(post0);
				Log.d("IVAN", "got: " + res20.getStatusLine());
				
				JSONObject responsee = getJSONObject(res20.getEntity().getContent());
				
				id = responsee.getInt("id");
			}
			HttpGet get = new HttpGet("http://thesnoops.herokuapp.com/snoop_assets/search?mdn=" + thisMdn);
			HttpResponse res = httpClient.execute(get);
			
			JSONObject responsee = getJSONObject(res.getEntity().getContent());
			
			id = responsee.getInt("id");
			Log.d("IVAN", "got: " + res.getStatusLine());
			
			HttpPost post = new HttpPost("http://thesnoops.herokuapp.com/activities");
			post.addHeader("Content-Type", "application/json");
			JSONObject obj = new JSONObject();
			obj.put("latitude", lat);
			obj.put("longitude", lng);
			obj.put("mdn", mdn);
			obj.put("asset_id", id);
			
			if (mdn.equals("911")) {
				obj.put("activity_type", "emergency");
			} else {
				obj.put("activity_type", "regular");
			}
			
			String content = obj.toString();
			post.setEntity(new StringEntity(content));
			
			HttpResponse res2 = httpClient.execute(post);
			Log.d("IVAN", "got: " + res2.getStatusLine());
		} catch (Exception e) {
			
		}
		return null;
//		JSONObject responseObject = JSON.getJSONObject(res.getEntity().getContent());
//		return responseObject;
	}
	
	@Override
	protected void onPostExecute(Void obj) {
		callback.onSuccess(null);
	}
	   
   }
   
   public static JSONObject getJSONObject(InputStream stream)
			throws JSONException {
		return getJSONObject(compileContent(stream));
	}
   
   public static JSONObject getJSONObject(String content) throws JSONException {
		return new JSONObject(content);
	}
   
   private static String compileContent(InputStream stream) {
		String compiledContent = "";
		Scanner s = new Scanner(stream);
		while (s.hasNext()) {
			compiledContent += s.next();
			if (s.hasNext()) {
				compiledContent += " ";
			}
		}
		return compiledContent;
	}
   
   private SharedPreferences getCallStateStore(Context c) {
      return c.getSharedPreferences(STATE_STORE, Context.MODE_PRIVATE);
   }

   private void saveState(Context c, String number, boolean incoming, long startTime) {
      Editor e = getCallStateStore(c).edit();
      e.putString(STATE_NUMBER_KEY, number);
      e.putBoolean(STATE_INCOMING_KEY, incoming);
      e.putLong(STATE_START_TIME_KEY, startTime);
      e.commit();
   }

   private void clearCallState(Context c) {
      Editor e = getCallStateStore(c).edit();
      e.clear();
      e.commit();
   }

//   private CallDetectionState getCurrentCallState(Context c) {
//      SharedPreferences css = getCallStateStore(c);
//      String number = css.getString(STATE_NUMBER_KEY, null);
//      if (number == null) { return null;}
//      boolean incoming = css.getBoolean(STATE_INCOMING_KEY, false);
//      long startTime = css.getLong(STATE_START_TIME_KEY, 0);
//         // duration is derived, don't store one here
//      CallDetectionState cds = new CallDetectionState(number, incoming, startTime, -1L);
//      return cds;
//   }

}
