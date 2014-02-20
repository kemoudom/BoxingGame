package com.challenge.boxinggame;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(SetupActivity.this, MainActivity.class);
				 startActivity(intent);
			}
		});
		
		Button exit= (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {
	    
			@Override
			public void onClick(View v) {
				//Exit the application
				finish();
				
			}
		});
		Button connection =(Button) findViewById(R.id.connection);
		connection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(SetupActivity.this, ConnexionManager.class);
				 //startActivity(intent);
			}
		});
	}	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup, menu);
		return true;
	}

}
