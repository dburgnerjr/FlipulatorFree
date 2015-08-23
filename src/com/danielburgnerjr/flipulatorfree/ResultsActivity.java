package com.danielburgnerjr.flipulatorfree;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends ActionBarActivity {
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);

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
		
		Intent intI = getIntent();
		
		calC = (Calculate) intI.getSerializableExtra("Calculate");
		
		etStreetAddress.setText(calC.getAddress());
		etCityStZipCode.setText(calC.getCityStZip());
		etSF.setText(calC.getSquareFootage() + "");
		etFMVARV.setText("$" + String.format("%.0f", calC.getFMVARV()));
		etSalesPrice.setText("$" + String.format("%.0f", calC.getSalesPrice()));
		etRehabBudget.setText("$" + String.format("%.0f", calC.getBudget()));
		
		resR = new Results();
		resR.setClosHoldCosts(calC.getFMVARV());
		resR.setProfit(calC.getSalesPrice(), calC.getFMVARV(), calC.getBudget());
		resR.setROI(calC.getFMVARV());
		resR.setCashOnCash(calC.getBudget());
		
		etClosHoldCosts.setText("$" + String.format("%.0f", resR.getClosHoldCosts()));
		etProfit.setText("$" + String.format("%.0f", resR.getProfit()));
		etROI.setText(String.format("%.1f", resR.getROI()) + "%");
		etCashOnCash.setText(String.format("%.1f", resR.getCashOnCash()) + "%");

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
