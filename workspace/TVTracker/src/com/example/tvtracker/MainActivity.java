package com.example.tvtracker;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private class loginThread extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPostExecute(Boolean result){
			loadHome(result);
		}

		@Override
		protected Boolean doInBackground(String... logins) {
			Boolean successfulLogin = false;
			SharedPreferences settings = getSharedPreferences("LoginInfo", 0);
		    SharedPreferences.Editor editor = settings.edit();
		    String username = logins[0];
			String password = logins[1];
		   
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			String checkLogin = "http://54.235.98.166/TVTracker/login.php?auth_username=" + username + "&auth_password=" + password;
			HttpPost httppost = new HttpPost(checkLogin); 
			try {
				HttpResponse response = client.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String text = EntityUtils.toString(entity);
					if (Integer.parseInt(text)==1){
						successfulLogin=true;
						editor.putString("username", username);
						editor.putString("password", password);
					    // Commit the edits!
						editor.commit();
					}
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return successfulLogin;
		}

	}
	Button register, login;
	EditText user, pass;
	TextView error;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		login = (Button)findViewById(R.id.loginButton);
		register = (Button)findViewById(R.id.registerButton);
		user = (EditText) findViewById(R.id.loginUserInput);
		pass = (EditText) findViewById(R.id.loginPwInput);
		error = (TextView)findViewById(R.id.error);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v){
		if (v==login){
			String[] information = {user.getText().toString(), pass.getText().toString()};
			if (information[0].length()==0 || information[0]==null || information[1].length()==0 || information[1]==null){
				return;
			}
			else {
				new loginThread().execute(information);
				return;
			}
		}
		if (v==register){
			error.setVisibility(View.INVISIBLE);
			Intent i = new Intent(this, Register.class);
			startActivity(i);
		}
	}
	
	private void loadHome(Boolean result) {
		// TODO Auto-generated method stub
		if (result){
			error.setVisibility(View.INVISIBLE);
			Intent i = new Intent(this, Home.class);
			startActivity(i);
		}
		else{
			error.setVisibility(View.VISIBLE);
		}
	}

}
