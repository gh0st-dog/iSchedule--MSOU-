package ru.romanov.schedule.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import ru.romanov.schedule.R;
import ru.romanov.schedule.src.UpdateDialogActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

public class Utils {
	public class UpdateChecker extends
		AsyncTask<Void, Void, Map<String, String>> {

		private String token;
		
		
		public UpdateChecker(String token) {
			this.token = token;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		
		@Override
		protected Map<String, String> doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			String reqString = null;
			try {
				reqString = RequestStringsCreater
						.createCheckUpdateString(token);
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpResponse responce = null;
			try {
				HttpPost request = new HttpPost(StringConstants.MY_URI);
				StringEntity entity = new StringEntity(reqString, "UTF-8");
				request.setHeader(HTTP.CONTENT_TYPE, "text/xml");
				request.setEntity(entity);
				responce = client.execute(request);
				HttpEntity ent = responce.getEntity();
		
				BufferedReader r = new BufferedReader(new InputStreamReader(
						ent.getContent()));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				Map<String, String> map = XMLParser
						.parseLastUpdateInfoResponse(total.toString());
				return map;
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
		protected void onPostExecute(Map<String, String> result) {
			super.onPostExecute(result);
			
		}
	}
	
	public class AsyncUpdater extends
		AsyncTask<Void, Void, MySubjectUpdateManager> {

		private String token;
	
		public AsyncUpdater(String token) {
			this.token = token;
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
		}
	}


}
