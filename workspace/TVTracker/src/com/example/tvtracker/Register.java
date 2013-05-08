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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends Activity implements OnClickListener{
	private class registerThread extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPostExecute(Boolean result){
			changeDisplay(result);		
			}
		


		@Override
		protected Boolean doInBackground(String... registers) {
			Boolean successfulRegister = false;
		    if (registers.length<4){
		    	return successfulRegister;
		    }
		    String username = registers[0];
			String password = registers[1];
			String fname = registers[2];
			String lname = registers[3];
		   
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			String checkRegister = "http://54.235.98.166/TVTracker/register.php?auth_username=" + username + "&auth_password=" + password + "&first_name=" + fname + "&last_name=" + lname;
			HttpPost httppost = new HttpPost(checkRegister); 
			try {
				HttpResponse response = client.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String text = EntityUtils.toString(entity);
					if (Integer.parseInt(text)==1){
						successfulRegister=true;
					    // Commit the edits!
					}
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return successfulRegister;
		}

	}
	
	Button register;
	EditText user, pass, fname, lname;
	TextView error1, error2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		register = (Button)findViewById(R.id.createUser);
		user = (EditText)findViewById(R.id.loginInput);
		pass = (EditText)findViewById(R.id.pwInput);
		fname = (EditText) findViewById(R.id.fnameInput);
		lname = (EditText) findViewById(R.id.lnameInput);
		error1 = (TextView) findViewById(R.id.registerErrorEmpty);
		error2 = (TextView) findViewById(R.id.registerErrorUser);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v==register){
			String[] information = {user.getText().toString(), pass.getText().toString(), fname.getText().toString(), lname.getText().toString()};
			if (information[0].length()==0 || information[0]==null || information[1].length()==0 || information[1]==null || information[2].length()==0 || information[2]==null || information[3].length()==0 || information[3]==null){
				error1.setVisibility(View.VISIBLE);
				return;
			}
			else {
				error1.setVisibility(View.GONE);
				new registerThread().execute(information);
				return;
			}
		}
		
	}
	private void changeDisplay(Boolean result){
		if (result){
			error2.setVisibility(View.GONE);
			finish();
		}
		else{
			error2.setVisibility(View.VISIBLE);
		}
	}
}
