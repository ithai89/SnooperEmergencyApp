package com.safely.snooper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

public class LocateAndSendService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void doLocate() {
//		LocationManager manager = (LocationManager) Context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public void sendData() {
		//HTTP REQUEST 
	}

}
