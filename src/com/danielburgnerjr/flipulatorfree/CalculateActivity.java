package com.danielburgnerjr.flipulatorfree;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CalculateActivity extends Activity {
	
	final Context cntC = this;
	private Calculate calR;				// Calculate object from ResultsActivity
	private Intent intR;				// Intent object from ResultsActivity
	
	private EditText etAddress;			// address
	private EditText etCityStZip;	    // city state zip code
	private EditText etSquareFootage;	// square footage
	private EditText etBedrooms;		// number of bedrooms
	private EditText etBathrooms;		// number of bathrooms
	private EditText etSalesPrice;		// sales price
	private EditText etFMVARV;			// fair mkt value/after repair value
	private RadioGroup rgRehab;			// radio group rehab
	private RadioButton rbRehab;		// rehab button id
	private RadioButton rbRehab1;		// rehab number
	private RadioButton rbRehab2;		// rehab type
	private TextView tvRehabFlatRate;	// rehab budget flat rate textview
	private EditText etRehabBudget;		// rehab budget
	private TextView tvRehabType;		// rehab type textview
	private Spinner spnRehabType;		// rehab type
	private Button btnHelp;				// help
	private double dB;					// budget

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate);
        AdView mAdCalcView = (AdView) findViewById(R.id.adCalcView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdCalcView.loadAd(adRequest);

		etAddress   = (EditText)findViewById(R.id.txtAddress);
		etCityStZip   = (EditText)findViewById(R.id.txtCityStZip);
		etSquareFootage   = (EditText)findViewById(R.id.txtSq_Footage);
		etBedrooms   = (EditText)findViewById(R.id.txtBedrooms);
		etBathrooms   = (EditText)findViewById(R.id.txtBathrooms);
		etSalesPrice   = (EditText)findViewById(R.id.txtSalePrice);
		etFMVARV   = (EditText)findViewById(R.id.txtFMVARV);
		btnHelp = (Button)findViewById(R.id.txtHelp);
		
		ArrayAdapter<CharSequence> aradAdapter = ArrayAdapter.createFromResource(
				  this, R.array.rehab_type, android.R.layout.simple_spinner_item );
		aradAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		ArrayAdapter<String> aradRehabType = new ArrayAdapter<String>(this, R.layout.rehab_type,R.array.rehab_type);
		
		rgRehab   = (RadioGroup)findViewById(R.id.rdoRehab);
		rbRehab1  = (RadioButton)findViewById(R.id.rdoRehabNumber);
		rbRehab2  = (RadioButton)findViewById(R.id.rdoRehabType);
		tvRehabFlatRate   = (TextView)findViewById(R.id.tvRehabBudget);
		etRehabBudget   = (EditText)findViewById(R.id.txtRehabBudget);
		tvRehabType   = (TextView)findViewById(R.id.tvRehabType);
		spnRehabType   = (Spinner)findViewById(R.id.spnRehabType);
		btnHelp = (Button)findViewById(R.id.txtHelp);
		spnRehabType.setAdapter(aradAdapter);

		tvRehabFlatRate.setVisibility(View.GONE);
		etRehabBudget.setVisibility(View.GONE);
		tvRehabType.setVisibility(View.GONE);
		spnRehabType.setVisibility(View.GONE);

		rgRehab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	        @Override
	        public void onCheckedChanged(RadioGroup rgG, int nChecked) {

	          if (nChecked == R.id.rdoRehabNumber) {
	        	  tvRehabFlatRate.setVisibility(View.VISIBLE);
	        	  etRehabBudget.setVisibility(View.VISIBLE);
	        	  tvRehabType.setVisibility(View.INVISIBLE);
	        	  spnRehabType.setVisibility(View.INVISIBLE);
	          } else if (nChecked == R.id.rdoRehabType) {
	        	  tvRehabFlatRate.setVisibility(View.INVISIBLE);
	        	  etRehabBudget.setVisibility(View.INVISIBLE);
	        	  tvRehabType.setVisibility(View.VISIBLE);
	        	  spnRehabType.setVisibility(View.VISIBLE);
	          } else {
	        	  tvRehabFlatRate.setVisibility(View.INVISIBLE);
	        	  etRehabBudget.setVisibility(View.INVISIBLE);
	        	  tvRehabType.setVisibility(View.INVISIBLE);
	        	  spnRehabType.setVisibility(View.INVISIBLE);
	          }
	        }
	    });

		
		// add button listener
		btnHelp.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntC);
 
				// set title
				alertDialogBuilder.setTitle("Calculate Help");
 
				// set dialog message
				alertDialogBuilder.setMessage("Enter the address and square footage of the property, " +
											  "including the number of bedrooms and bathrooms, sale price, ARV and rehab budget. " +
											  "Rehab budget can be a flat rate or a rehab type. " +
											  "Rehab types are classified as:  Low ($15/sf, yard work and painting), " +
											  "Medium ($20/sf > 1500 sf or $25/sf < 1500 sf, Low + kitchen and bathrooms, " +
											  "High ($30/sf, Medium + new roof), Super-High ($40/sf, complete gut job), " +
											  "Bulldozer ($125/sf, demolition and rebuild).")
								  .setCancelable(false)
								  .setNeutralButton("OK", new DialogInterface.OnClickListener() {
									  public void onClick(DialogInterface dialog, int id) {
										  // if this button is clicked, close
										  // current activity
										  dialog.cancel();
									  }
								  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			}
		});
		
		// gets Intent and Calculate object
		intR = getIntent();
		calR = (Calculate) intR.getSerializableExtra("Calculate");
		// if Calculate object is null, fields are blank
		if (calR == null) {
			etAddress.setText("");
			etCityStZip.setText("");
			etSquareFootage.setText("");
			etBedrooms.setText("");
			etBathrooms.setText("");
			etSalesPrice.setText("");
			etFMVARV.setText("");
		} else {
			// set fields to member variables of Calculate object
			etAddress.setText(calR.getAddress());
			etCityStZip.setText(calR.getCityStZip());
			etSquareFootage.setText(calR.getSquareFootage() + "");
			etBedrooms.setText(calR.getBedrooms() + "");
			etBathrooms.setText(calR.getBathrooms() + "");
			etSalesPrice.setText((int)calR.getSalesPrice() + "");
			etFMVARV.setText((int)calR.getFMVARV() + "");
			if (calR.getRehabFlag() == 0) {
				  rbRehab1.setChecked(true);
				  rbRehab2.setChecked(false);
	        	  tvRehabFlatRate.setVisibility(View.VISIBLE);
	        	  etRehabBudget.setVisibility(View.VISIBLE);
	        	  tvRehabType.setVisibility(View.INVISIBLE);
	        	  spnRehabType.setVisibility(View.INVISIBLE);
	        	  etRehabBudget.setText((int)calR.getBudget() + "");
			} else {
				  rbRehab1.setChecked(false);
				  rbRehab2.setChecked(true);
	        	  tvRehabFlatRate.setVisibility(View.INVISIBLE);
	        	  etRehabBudget.setVisibility(View.INVISIBLE);
	        	  tvRehabType.setVisibility(View.VISIBLE);
	        	  spnRehabType.setVisibility(View.VISIBLE);
	        	  int nCostSF = (int)(calR.getBudget()/calR.getSquareFootage());
	        	  switch (nCostSF) {
	        	  		case 15:
	        	  					spnRehabType.setSelection(0);
	        	  					break;
	        	  		case 20:
	        	  		case 25:
		    	  					spnRehabType.setSelection(1);
		    	  					break;
	        	  		case 30:
		    	  					spnRehabType.setSelection(2);
		    	  					break;
	        	  		case 40:
		    	  					spnRehabType.setSelection(3);
		    	  					break;
	        	  		case 125:
		    	  					spnRehabType.setSelection(4);
		    	  					break;
	        	  }
			}
		}
	}

	public void nextPage(View view) {
		// checks if all fields are filled in, prompts user to fill in fields if any are missing
		if (("").equals(etAddress.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Must Enter Address", Toast.LENGTH_SHORT).show();
		} else if (("").equals(etCityStZip.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Must Enter City/State/Zip", Toast.LENGTH_SHORT).show();
		} else if (("").equals(etSquareFootage.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Must Enter Square Footage", Toast.LENGTH_SHORT).show();
		} else if (("").equals(etBedrooms.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Must Enter Bedrooms", Toast.LENGTH_SHORT).show();
		} else if (("").equals(etBathrooms.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Must Enter Bathrooms", Toast.LENGTH_SHORT).show();
		} else if (("").equals(etSalesPrice.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Must Enter Sales Price", Toast.LENGTH_SHORT).show();
		} else if (("").equals(etFMVARV.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Must Enter Fair Market Value or After Repair Value", Toast.LENGTH_SHORT).show();
		} else {
			Intent intI = new Intent(CalculateActivity.this, ResultsActivity.class);
	    
			// creates new Calculate object and sets info from fields into object variables
			Calculate calC = new Calculate();
			calC.setAddress(etAddress.getText().toString());
			calC.setCityStZip(etCityStZip.getText().toString());
			calC.setSquareFootage(Integer.parseInt(etSquareFootage.getText().toString()));
			calC.setBedrooms(Integer.parseInt(etBedrooms.getText().toString()));
			calC.setBathrooms(Integer.parseInt(etBathrooms.getText().toString()));
			calC.setFMVARV(Integer.parseInt(etFMVARV.getText().toString()));
			calC.setSalesPrice(Integer.parseInt(etSalesPrice.getText().toString()));
			int nSelected = rgRehab.getCheckedRadioButtonId();
			rbRehab = (RadioButton)findViewById(nSelected);
			
			// determines Rehab object by radio button input
			switch (nSelected) {
			
				case R.id.rdoRehabNumber:
											if (("").equals(etRehabBudget.getText().toString())) {
												Toast.makeText(getApplicationContext(), "Must Enter Rehab Budget", Toast.LENGTH_SHORT).show();
											} else {
												dB = Double.parseDouble(etRehabBudget.getText().toString());
												calC.setBudget(dB);
											}
											calC.setRehabFlag(0);
											break;
											
				case R.id.rdoRehabType:
											String strRTSel = spnRehabType.getSelectedItem().toString();
											calC.calcBudgetRehabType(strRTSel);
											calC.setRehabFlag(1);
											break;
											
			}
			
			// stores Calculate object in Intent
			intI.putExtra("Calculate", calC);	    
			startActivity(intI);
			finish();
		}
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
		    .setMessage("Do you want to go back to main menu?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        // do something when the button is clicked
		        public void onClick(DialogInterface arg0, int arg1) {
		        	Intent intB = new Intent(CalculateActivity.this, MainActivity.class);
		        	startActivity(intB);
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
