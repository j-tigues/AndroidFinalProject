package com.example.tvtracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity implements OnClickListener {

	Button viewStats, viewShows, searchShows, top25;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		viewStats = (Button)findViewById(R.id.viewTimeStatsButton);
		viewShows = (Button)findViewById(R.id.viewShowsButton);
		searchShows = (Button)findViewById(R.id.searchShowsButton);
		top25 = (Button)findViewById(R.id.top25Button);
		viewStats.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
