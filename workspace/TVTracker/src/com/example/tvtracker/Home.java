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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Home extends Activity implements OnClickListener {
	private class viewMyShows extends AsyncTask<Void, Void, ArrayList<String>> {
		@Override
		protected void onPostExecute(ArrayList<String> result){
			loadMyShows(result);
		}

		@Override
		protected ArrayList<String> doInBackground(Void...flag) {
			ArrayList<String> listOfShows = new ArrayList<String>();
			SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		    String username = settings.getString("username", "");
		   
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			String url ="";
			url = "http://54.235.98.166/TVTracker/androidhandler.php?auth_username=" + username + "&option=viewMyShows";
			HttpPost httppost = new HttpPost(url); 
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
	private class addShows extends AsyncTask<Integer, Void, ArrayList<String>> {
		boolean[] viewed;
		int check;
		@Override
		protected void onPostExecute(ArrayList<String> result){
			if (check==0){
				loadAllShows(result, viewed);
			}
			else if (check==1){
				loadTopShows(result, viewed);
			}
		}

		@Override
		protected ArrayList<String> doInBackground(Integer...flag) {
			ArrayList<String> listOfShows = new ArrayList<String>();
			SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		    String username = settings.getString("username", "");
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			String checkLogin = "";
			if (flag[0]==0){
				checkLogin = "http://54.235.98.166/TVTracker/androidhandler.php?option=viewAllShows";
			}
			else if (flag[0]==1){
				checkLogin = "http://54.235.98.166/TVTracker/androidhandler.php?option=viewTopShows";
			}
			check = flag[0];

			HttpPost httppost = new HttpPost(checkLogin); 
			try {
				HttpResponse response = client.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String text = EntityUtils.toString(entity);
					JSONArray notebooksArray = new JSONArray(text);
					viewed = new boolean[notebooksArray.length()];
					int j=0;
					for (int i=0; i<notebooksArray.length(); i++){
						JSONObject tmp = notebooksArray.getJSONObject(i);
						if (tmp.getString("username").equals(username)){
							viewed[j]=true;
						}
						if (!listOfShows.contains(tmp.getString("title"))){
							listOfShows.add(tmp.getString("title"));
							j++;
						}
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
	
	private class updateText extends AsyncTask<Void, Void, String>{
		@Override
		protected void onPostExecute(String result){
			updateText(result);
		}

		@Override
		protected String doInBackground(Void...data) {
			SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		    String username = settings.getString("username", "");
		    String password = settings.getString("password", "");
		    String returnThis = "";
		   
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			String url ="";
			url = "http://54.235.98.166/TVTracker/login.php?auth_username=" + username + "&auth_password=" +password;
			HttpPost httppost = new HttpPost(url); 
			try {
				HttpResponse response = client.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String text = EntityUtils.toString(entity);
					if (!text.equals("0")){
						JSONObject tmp = new JSONObject(text);
						String AvgRating;
						if (Float.parseFloat(tmp.getString("numRatings"))==0){
							AvgRating = String.format("%.2f",0.00/0.00);
						}
						else{
							AvgRating = String.format("%.2f", Float.parseFloat(tmp.getString("totalRating"))/Float.parseFloat(tmp.getString("numRatings")));
						}
						returnThis = "Name: " + tmp.getString("fname") + " " + tmp.getString("lname") + '\n' + '\n'+ 
								"Shows watching: " + tmp.getString("numShows") + '\n' + '\n'
								+ "Average Rating: " + AvgRating + " / 5.0";
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
			
			return returnThis;
		}
	}
	
	Button myShows, addShows, topShows;
	TextView welcomeView, centerText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		myShows = (Button)findViewById(R.id.myShowsButton);
		addShows = (Button)findViewById(R.id.addShowsButton);
		topShows = (Button)findViewById(R.id.mostWatchedButton);
		welcomeView = (TextView)findViewById(R.id.welcomeText);
		centerText = (TextView) findViewById(R.id.homeText);
		myShows.setOnClickListener(this);
		addShows.setOnClickListener(this);
		topShows.setOnClickListener(this);
		Intent i = this.getIntent();
		Bundle b = i.getExtras();
		SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		String text = "Welcome, " + settings.getString("username", "");
		welcomeView.setText(text);
		String AvgRating = String.format("%.2f", Float.parseFloat(b.getString("ratingTotal"))/Float.parseFloat(b.getString("numRatings")));
		text = "Name: " + b.getString("fname") + " " + b.getString("lname") + '\n' + '\n'+ 
				"Shows watching: " + b.getString("numShows") + '\n' + '\n'
				+ "Average Rating: " + AvgRating + " / 5.0";
		centerText.setText(text);
	}

	public void loadAllShows(ArrayList<String> result, boolean[] watched) {
		Bundle b = new Bundle();
		b.putStringArrayList("shows", result);
		b.putBooleanArray("watchedShows", watched);
		Intent i = new Intent(this, Shows.class);
		i.putExtras(b);
		startActivityForResult(i,0);
	}
	
	public void loadMyShows(ArrayList<String> result) {
		Bundle b = new Bundle();
		b.putStringArrayList("shows", result);
		Intent i = new Intent(this, MyShows.class);
		i.putExtras(b);
		startActivityForResult(i,0);
	}
	public void loadTopShows(ArrayList<String> result,  boolean[] watched) {
		Bundle b = new Bundle();
		b.putStringArrayList("shows", result);
		b.putBooleanArray("watchedShows", watched);
		Intent i = new Intent(this, TopShows.class);
		i.putExtras(b);
		startActivityForResult(i,0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.logoutMenu:
	            // app icon in action bar clicked; go home
	            terminate(0);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onClick(View v) {
		if (v==myShows){
			new viewMyShows().execute();
		}
		else if(v==addShows){
			new addShows().execute(0);
		}
		else if (v==topShows){
			new addShows().execute(1);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if(resultCode == RESULT_OK){      
				int result=data.getIntExtra("flag", 1);   
				if (result==0){
					terminate(0);
				}
			}
		}
	}
	@Override
	protected void onRestart() {
	    super.onRestart();
	    new updateText().execute();
	}
	private void updateText(String data){
		centerText.setText(data);
	}
	

}
