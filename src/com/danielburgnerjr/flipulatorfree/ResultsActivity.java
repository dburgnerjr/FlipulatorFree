package com.danielburgnerjr.flipulatorfree;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity {
	
	final Context cntC = this;
	
	private Calculate calC;
	private Results resR;

	private EditText etStreetAddress;	// address
	private EditText etCityStZipCode;   // city state zip code
	private EditText etSF;			    // sq ft/number of BR/number of BA
	private EditText etSalesPrice;		// sales price
	private EditText etFMVARV;			// fair mkt value/after repair value
	private EditText etRehabBudget;		// rehab budget
	private EditText etClosHoldCosts;	// closing/holding costs
	private EditText etProfit;			// profit
	private EditText etROI;				// return on investment
	private EditText etCashOnCash;		// cash on cash return
	private EditText etBudgetItems;		// budget items

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
        AdView mAdResView = (AdView) findViewById(R.id.adResView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdResView.loadAd(adRequest);

		etStreetAddress = (EditText)findViewById(R.id.txtStreetAddress);
		etCityStZipCode = (EditText)findViewById(R.id.txtCityStZipCode);
		etSF  = (EditText)findViewById(R.id.txtSq_Ft);
		etFMVARV = (EditText)findViewById(R.id.txtFMVARVResult);
		etSalesPrice = (EditText)findViewById(R.id.txtSalePriceResult);		
		etRehabBudget = (EditText)findViewById(R.id.txtRehabBudgetResult);
		etClosHoldCosts = (EditText)findViewById(R.id.txtClosHoldCosts);		
		etProfit = (EditText)findViewById(R.id.txtProfit);
		etROI = (EditText)findViewById(R.id.txtROI);		
		etCashOnCash = (EditText)findViewById(R.id.txtCashOnCash);
		etBudgetItems = (EditText)findViewById(R.id.txtBudgetItems);
		
		etStreetAddress.setKeyListener(null);
		etStreetAddress.setEnabled(false);
		etCityStZipCode.setKeyListener(null);
		etCityStZipCode.setEnabled(false);
		etSF.setKeyListener(null);
		etSF.setEnabled(false);
		etFMVARV.setKeyListener(null);
		etFMVARV.setEnabled(false);
		etSalesPrice.setKeyListener(null);
		etSalesPrice.setEnabled(false);
		etRehabBudget.setKeyListener(null);
		etRehabBudget.setEnabled(false);
		etClosHoldCosts.setKeyListener(null);
		etClosHoldCosts.setEnabled(false);
		etProfit.setKeyListener(null);
		etProfit.setEnabled(false);
		etROI.setKeyListener(null);
		etROI.setEnabled(false);
		etCashOnCash.setKeyListener(null);
		etCashOnCash.setEnabled(false);
		etBudgetItems.setKeyListener(null);
		etBudgetItems.setEnabled(false);
		
		Intent intI = getIntent();
		
		calC = (Calculate) intI.getSerializableExtra("Calculate");
		
		etStreetAddress.setText(calC.getAddress());
		etCityStZipCode.setText(calC.getCityStZip());
		etSF.setText(calC.getSquareFootage() + "");
		etFMVARV.setText("$" + String.format("%.0f", calC.getFMVARV()));
		etSalesPrice.setText("$" + String.format("%.0f", calC.getSalesPrice()));
		etRehabBudget.setText("$" + String.format("%.0f", calC.getBudget()));
		etBudgetItems.setText(calC.getBudgetItems());
		
		resR = new Results();
		resR.setClosHoldCosts(calC.getFMVARV());
		resR.setProfit(calC.getSalesPrice(), calC.getFMVARV(), calC.getBudget());
		resR.setROI(calC.getFMVARV());
		if (resR.getProfit() < 30000.0) {
			AlertDialog adAlertBox = new AlertDialog.Builder(this)
		    .setMessage("Your profit is below $30K! Would you like to make changes now?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        // do something when the button is clicked
		        public void onClick(DialogInterface arg0, int arg1) {
		        	Intent intB = new Intent(ResultsActivity.this, CalculateActivity.class);
		        	intB.putExtra("Calculate", calC);
		        	startActivity(intB);
		        	finish();
		        }
		    })
		    .setNegativeButton("No", new DialogInterface.OnClickListener() {
		        // do something when the button is clicked
		        public void onClick(DialogInterface arg0, int arg1) {
		        }
		    })
		    .show();
		}
		resR.setCashOnCash(calC.getBudget());
		
		etClosHoldCosts.setText("$" + String.format("%.0f", resR.getClosHoldCosts()));
		etProfit.setText("$" + String.format("%.0f", resR.getProfit()));
		etROI.setText(String.format("%.1f", resR.getROI()) + "%");
		etCashOnCash.setText(String.format("%.1f", resR.getCashOnCash()) + "%");

	}

	// returns to main menu
	public void mainMenu(View view) {
	    Intent intI = new Intent(ResultsActivity.this, MainActivity.class);
	    startActivity(intI);
	    finish();
	};

	// returns to Calculate to edit any information
	public void editInfo (View view) {
		Intent intI = new Intent(ResultsActivity.this, CalculateActivity.class);
		intI.putExtra("Calculate", calC);
		startActivity(intI);
		finish();
	}
	
	public void nextPage(View view) {
		// email results of calculate to those parties concerned
		String strMessage = "Address:                " + calC.getAddress() + "\n";
		strMessage += "City, State ZIP:        " + calC.getCityStZip() + "\n";
		strMessage += "Square Footage:         " + calC.getSquareFootage() + "\n";
		strMessage += "Bedrooms/Bathrooms:     " + calC.getBedrooms() + " BR " + calC.getBathrooms() + " BA\n";
		strMessage += "After Repair Value:    $" + String.format("%.0f", calC.getFMVARV()) + "\n";
		strMessage += "Sales Price:           $" + String.format("%.0f", calC.getSalesPrice()) + "\n";
		strMessage += "Estimated Budget:      $" + String.format("%.0f", calC.getBudget()) + "\n";
		strMessage += "Budget Items:           " + calC.getBudgetItems() + "\n";
		strMessage += "Closing/Holding Costs: $" + String.format("%.0f", resR.getClosHoldCosts()) + "\n";
		strMessage += "Profit:                $" + String.format("%.0f", resR.getProfit()) + "\n";
		strMessage += "ROI:                    " + String.format("%.1f", resR.getROI()) + "%\n";
		strMessage += "Cash on Cash Return:    " + String.format("%.1f", resR.getCashOnCash()) + "%\n";
		Intent intEmailActivity = new Intent(Intent.ACTION_SEND);
		intEmailActivity.putExtra(Intent.EXTRA_EMAIL, new String[]{});
		intEmailActivity.putExtra(Intent.EXTRA_SUBJECT, "Flipulator Free results for: " + calC.getAddress() + " " + calC.getCityStZip());
		intEmailActivity.putExtra(Intent.EXTRA_TEXT, strMessage);
		intEmailActivity.setType("plain/text");
   		startActivity(intEmailActivity);
	}
	
	 public boolean onKeyDown(int nKeyCode, KeyEvent keEvent) {
		String strBackMessage = "Press Edit to make changes, Main Menu to return to main menu ";
		strBackMessage += " or Email Results to email.";
		if (nKeyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(getApplicationContext(), strBackMessage, Toast.LENGTH_SHORT).show();
		    return true;
		}
		return super.onKeyDown(nKeyCode, keEvent);
    }
}
