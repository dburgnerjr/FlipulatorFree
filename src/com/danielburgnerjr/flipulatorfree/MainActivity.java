package com.danielburgnerjr.flipulatorfree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {
	
	String strPackName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		strPackName = getApplicationContext().getPackageName();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        
		final Button btnAbout = (Button) findViewById(R.id.btnAbout);
		btnAbout.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
			    Intent intI = new Intent(MainActivity.this, AboutFlipulatorFree.class);
			    startActivity(intI);
			}
		});

		final Button btnCalculate = (Button) findViewById(R.id.btnCalculate);
		btnCalculate.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
			    Intent intI = new Intent(MainActivity.this, CalculateActivity.class);
			    startActivity(intI);
			}
		});

		final Button btnUpgrade = (Button) findViewById(R.id.btnUpgrade);
		btnUpgrade.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
			    Intent intI = new Intent(MainActivity.this, UpgradeActivity.class);
			    startActivity(intI);
			}
		});

		final Button btnDonate = (Button) findViewById(R.id.btnDonate);
		btnDonate.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
			    Intent intI = new Intent(MainActivity.this, DonateActivity.class);
			    startActivity(intI);
			}
		});

		final Button btnShare = (Button) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
	   			Intent intI = new Intent(Intent.ACTION_SEND);  
	   			intI.setType("text/plain");
	   			intI.putExtra(Intent.EXTRA_SUBJECT, "Flipulator Free");
	   			String sAux = "\nLet me recommend you this application\n\n";
	   			sAux = sAux + "https://play.google.com/store/apps/details?id=" + strPackName + "\n\n";
	   			intI.putExtra(Intent.EXTRA_TEXT, sAux);  
	   			startActivity(Intent.createChooser(intI, "choose one"));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
