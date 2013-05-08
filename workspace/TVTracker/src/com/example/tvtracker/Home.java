package com.example.tvtracker;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Home extends Activity implements OnClickListener {
	private class viewShows extends AsyncTask<Integer, Void, ArrayList<String>> {

		@Override
		protected void onPostExecute(ArrayList<String> result){
			loadAllShows(result);
		}

		@Override
		protected ArrayList<String> doInBackground(Integer...flag) {
			ArrayList<String> listOfShows = new ArrayList<String>();
			SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		    String username = settings.getString("username", "");
		   
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			String checkLogin;
			if (flag[0]==0){
				checkLogin = "http://54.235.98.166/TVTracker/androidhandler.php?auth_username=" + username + "&option=viewMyShows";
			}
			else if (flag[0]==1){
				checkLogin = "http://54.235.98.166/TVTracker/androidhandler.php?option=viewAllShows";
			}
			else{
				checkLogin = "http://54.235.98.166/TVTracker/androidhandler.php?option=viewTopShows";
			}
			HttpPost httppost = new HttpPost(checkLogin); 
			try {
				HttpResponse response = client.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String text = EntityUtils.toString(entity);
					JSONArray notebooksArray = new JSONArray(text);
					for (int i=0; i<notebooksArray.length(); i++){
						JSONObject tmp = notebooksArray.getJSONObject(i);
						listOfShows.add(tmp.getString("title"));
					}
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return listOfShows;
		}

	}
	Button myShows, addShows, topShows;
	TextView welcomeView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		myShows = (Button)findViewById(R.id.myShowsButton);
		addShows = (Button)findViewById(R.id.addShowsButton);
		topShows = (Button)findViewById(R.id.mostWatchedButton);
		welcomeView = (TextView)findViewById(R.id.welcomeText);
		myShows.setOnClickListener(this);
		addShows.setOnClickListener(this);
		topShows.setOnClickListener(this);
		SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		String text = "Welcome, " + settings.getString("username", "");
		welcomeView.setText(text);
		
	}

	public void loadAllShows(ArrayList<String> result) {
		Bundle b = new Bundle();
		b.putStringArrayList("shows", result);
		Intent i = new Intent(this, Shows.class);
		i.putExtras(b);
		startActivityForResult(i,0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v==myShows){
			new viewShows().execute(0);
		}
		else if(v==addShows){
			new viewShows().execute(1);
		}
		else if (v==topShows){
			//new viewShows().execute(2);
		}
		
	}
	
	@Override
	public void onBackPressed() {
	    terminate(0);
	}

	private void terminate(int flag) {
		// 0 : logout regularly 
		if (flag==0){
			clearLoginInfo();
			finish();
		}
	}

	private void clearLoginInfo() {
		SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove("username");
		editor.remove("password");
		editor.commit();
		
	}
	

}
