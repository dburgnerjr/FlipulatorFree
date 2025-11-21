package com.danielburgnerjr.flipulatorfree;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        File myDir = new File(getApplicationContext().getExternalFilesDir(null) + "/FlipulatorFree");
		String strPath = myDir.getPath();
        RateThisApp.onLaunch(this);
        
		final Button btnAbout = findViewById(R.id.btnAbout);
		btnAbout.setOnClickListener(view -> {
            Intent intI = new Intent(MainActivity.this, AboutFlipulatorFree.class);
            startActivity(intI);
        });

		final Button btnCalculate = findViewById(R.id.btnCalculate);
		btnCalculate.setOnClickListener(view -> {
            Intent intI = new Intent(MainActivity.this, CalculateActivity.class);
            startActivity(intI);
            finish();
        });

		final Button btnUpgrade = findViewById(R.id.btnUpgrade);
		btnUpgrade.setOnClickListener(view -> {
            Uri uri = Uri.parse(getResources().getString(R.string.market_premium));
            Intent newActivity = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(newActivity);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_premium))));
            }
        });

		final Button btnDonate = findViewById(R.id.btnDonate);
		btnDonate.setOnClickListener(view -> {
            Intent intI = new Intent(MainActivity.this, DonateActivity.class);
            startActivity(intI);
        });

		final Button btnShare = findViewById(R.id.btnShare);
		btnShare.setOnClickListener(view -> {
               Intent intI = new Intent(Intent.ACTION_SEND);
               intI.setType("text/plain");
               intI.putExtra(Intent.EXTRA_SUBJECT, "Flipulator Free");
               String sAux = "\nLet me recommend you this application\n\n";
               sAux = sAux + "https://play.google.com/store/apps/details?id=" + strPackName + "\n\n";
               intI.putExtra(Intent.EXTRA_TEXT, sAux);
               startActivity(Intent.createChooser(intI, "choose one"));
        });

        Button btnOpenFiles = findViewById(R.id.btnOpenFiles);
		File fFileList = new File(strPath);
		File[] fFileArray = fFileList.listFiles();
		if (fFileArray == null) {
			btnOpenFiles.setVisibility(View.INVISIBLE);
		}
		btnOpenFiles.setOnClickListener(view -> {
            Intent intI = new Intent(MainActivity.this, OpenFilesActivity.class);
            startActivity(intI);
            finish();
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
