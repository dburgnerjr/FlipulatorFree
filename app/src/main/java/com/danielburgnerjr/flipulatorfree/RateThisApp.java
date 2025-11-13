package com.danielburgnerjr.flipulatorfree;

import android.app.Dialog;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RateThisApp {
	private final static String APP_TITLE = "Flipulator Free";
	private final static String APP_PNAME = "com.danielburgnerjr.flipulatorfree";

	private final static int DAYS_UNTIL_PROMPT = 3;
	private final static int LAUNCHES_UNTIL_PROMPT = 7;

	public static void onLaunch(Context mContext) {
	    SharedPreferences spPrefs = mContext.getSharedPreferences("ratethisapp", 0);
	    if (spPrefs.getBoolean("dontshowagain", false)) { return ; }
	
	    SharedPreferences.Editor speEditor = spPrefs.edit();
	
	    // Increment launch counter
	    long lLaunchCount = spPrefs.getLong("lLaunchCount", 0) + 1;
	    speEditor.putLong("lLaunchCount", lLaunchCount);
	
	    // Get date of first launch
	    long lDateFirstLaunch = spPrefs.getLong("lDateFirstLaunch", 0);
	    if (lDateFirstLaunch == 0) {
	    	lDateFirstLaunch = System.currentTimeMillis();
	        speEditor.putLong("lDateFirstLaunch", lDateFirstLaunch);
	    }
	
	    // Wait at least n days before opening
	    if (lLaunchCount >= LAUNCHES_UNTIL_PROMPT) {
	        if (System.currentTimeMillis() >= lDateFirstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
	            showRateDialog(mContext, speEditor);
	        }
	    }
	
	    speEditor.apply();
	}   
	
	public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
	    final Dialog dialog = new Dialog(mContext);
	    dialog.setTitle("Rate " + APP_TITLE);

	    LinearLayout ll = new LinearLayout(mContext);
	    ll.setOrientation(LinearLayout.VERTICAL);
	    ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
	
	    TextView tv = new TextView(mContext);
	    tv.setTextColor(Color.parseColor("#000000"));
	    tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
	    tv.setWidth(240);
	    tv.setPadding(4, 0, 4, 10);
	    ll.addView(tv);
	
	    Button b1 = new Button(mContext);
	    b1.setText("Rate " + APP_TITLE);
	    b1.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
	            if (editor != null) {
	                editor.putBoolean("dontshowagain", true);
	                editor.commit();
	            }
	            dialog.dismiss();
	        }
	    });        
	    ll.addView(b1);
	
	    Button b2 = new Button(mContext);
	    b2.setText("Remind me later");
	    b2.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            if (editor != null) {
	                editor.putLong("lLaunchCount", 0);
	                editor.putLong("lDateFirstLaunch", 0);
	                editor.commit();
	            }
	            dialog.dismiss();
	        }
	    });
	    ll.addView(b2);
	
	    Button b3 = new Button(mContext);
	    b3.setText("No, thanks");
	    b3.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            if (editor != null) {
	                editor.putBoolean("dontshowagain", true);
	                editor.commit();
	            }
	            dialog.dismiss();
	        }
	    });
	    ll.addView(b3);
	
	    dialog.setContentView(ll);        
	    dialog.show();        
	}
}