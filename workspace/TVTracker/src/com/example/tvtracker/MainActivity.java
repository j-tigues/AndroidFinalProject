package com.example.tvtracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button register, login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		login = (Button)findViewById(R.id.loginButton);
		register = (Button)findViewById(R.id.registerButton);
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
			Intent i = new Intent(this, Home.class);
			startActivity(i);
		}
		if (v==register){
			Intent i = new Intent(this, Register.class);
			startActivity(i);
		}
	}

}
