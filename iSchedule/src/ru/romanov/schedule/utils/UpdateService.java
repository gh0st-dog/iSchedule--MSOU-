package ru.romanov.schedule.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;

public class UpdateService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences mSharedPreferences = getSharedPreferences(
				StringConstants.SCHEDULE_SHARED_PREFERENCES, MODE_PRIVATE);
		String token = mSharedPreferences.getString(StringConstants.TOKEN, null);
		if (token == null)
			return;
		if (isNetworkAvailable()){
			AsyncUpdater updater = new AsyncUpdater(this, token);
			updater.execute();
		}
		
	}
	
	private class AsyncUpdater extends
		AsyncTask<Void, Void, MySubjectUpdateManager> {

		private String token;
		private Context context;
	
		public AsyncUpdater(Context context, String token) {
			this.token = token;
			this.context = context;
		}
	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	
		@Override
		protected MySubjectUpdateManager doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			String reqString = null;
			try {
				reqString = RequestStringsCreater.createUpdateString(token);
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpResponse response = null;
			try {
				HttpPost request = new HttpPost(StringConstants.MY_URI);
				StringEntity entity = new StringEntity(reqString, "UTF-8");
				request.setHeader(HTTP.CONTENT_TYPE, "text/xml");
				request.setEntity(entity);
				response = client.execute(request);
				HttpEntity ent = response.getEntity();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						ent.getContent()));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				MySubjectUpdateManager msManager = XMLParser
						.parseXMLScheduleResponse(total.toString());
				return msManager;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
	
			}
			return null;
	
		}
		
	
		@Override
		protected void onPostExecute(MySubjectUpdateManager updateManager) {
			super.onPostExecute(updateManager);
			if (updateManager != null) {
				UpdateManager.notificateAboutUpdate(context);
			}
			stopSelf();
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}

}
