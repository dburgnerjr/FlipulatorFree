package com.danielburgnerjr.flipulatorfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
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
        RateThisApp.onLaunch(this);
        
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
			    finish();
			}
		});

		final Button btnUpgrade = (Button) findViewById(R.id.btnUpgrade);
		btnUpgrade.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
		        Uri uri = Uri.parse(getResources().getString(R.string.market_premium));
		        Intent newActivity = new Intent(Intent.ACTION_VIEW, uri);
		        try {
		            startActivity(newActivity);
		        } catch (ActivityNotFoundException e) {
		            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_premium))));
		        }
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
	
	 public boolean onKeyDown(int nKeyCode, KeyEvent keEvent) {
		if (nKeyCode == KeyEvent.KEYCODE_BACK) {
			exitByBackKey();
		    return true;
		}
		return super.onKeyDown(nKeyCode, keEvent);
     }

	 protected void exitByBackKey() {
		AlertDialog adAlertBox = new AlertDialog.Builder(this)
		    .setMessage("Do you want to exit application?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        // do something when the button is clicked
		        public void onClick(DialogInterface arg0, int arg1) {
		            finish();
		            //close();
		        }
		    })
		    .setNegativeButton("No", new DialogInterface.OnClickListener() {
		        // do something when the button is clicked
		        public void onClick(DialogInterface arg0, int arg1) {
		        }
		    })
		    .show();
	 }
}
