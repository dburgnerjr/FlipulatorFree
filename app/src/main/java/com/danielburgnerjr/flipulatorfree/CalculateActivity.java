package com.danielburgnerjr.flipulatorfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CalculateActivity extends Activity {
	
	final Context cntC = this;

    private EditText etAddress;			// address
	private EditText etCityStZip;	    // city state zip code
	private EditText etSquareFootage;	// square footage
	private EditText etBedrooms;		// number of bedrooms
	private EditText etBathrooms;		// number of bathrooms
	private EditText etSalesPrice;		// sales price
	private EditText etFMVARV;			// fair mkt value/after repair value
	private EditText etBudgetItems;		// budget items
	private RadioGroup rgRehab;			// radio group rehab
    private TextView tvRehabFlatRate;	// rehab budget flat rate textview
	private EditText etRehabBudget;		// rehab budget
	private TextView tvRehabType;		// rehab type textview
	private Spinner spnRehabType;		// rehab type

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate);
        AdView mAdCalcView = findViewById(R.id.adCalcView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdCalcView.loadAd(adRequest);

        etAddress = findViewById(R.id.txtAddress);
		etCityStZip = findViewById(R.id.txtCityStZip);
		etSquareFootage = findViewById(R.id.txtSq_Footage);
		etBedrooms = findViewById(R.id.txtBedrooms);
		etBathrooms = findViewById(R.id.txtBathrooms);
		etSalesPrice = findViewById(R.id.txtSalePrice);
		etFMVARV = findViewById(R.id.txtFMVARV);
		etBudgetItems = findViewById(R.id.txtBudgetItems);
        // help
        Button btnHelp = findViewById(R.id.txtHelp);
		
		ArrayAdapter<CharSequence> aradAdapter = ArrayAdapter.createFromResource(
				  this, R.array.rehab_type, android.R.layout.simple_spinner_item );
		aradAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		//ArrayAdapter<String> aradRehabType = new ArrayAdapter<>(this, R.layout.rehab_type, R.array.rehab_type);
		
		rgRehab = findViewById(R.id.rdoRehab);
        // rehab number
        RadioButton rbRehab1 = findViewById(R.id.rdoRehabNumber);
        // rehab type
        RadioButton rbRehab2 = findViewById(R.id.rdoRehabType);
		tvRehabFlatRate = findViewById(R.id.tvRehabBudget);
		etRehabBudget = findViewById(R.id.txtRehabBudget);
		tvRehabType = findViewById(R.id.tvRehabType);
		spnRehabType = findViewById(R.id.spnRehabType);
		btnHelp = findViewById(R.id.txtHelp);
		spnRehabType.setAdapter(aradAdapter);

		tvRehabFlatRate.setVisibility(View.GONE);
		etRehabBudget.setVisibility(View.GONE);
		tvRehabType.setVisibility(View.GONE);
		spnRehabType.setVisibility(View.GONE);

		rgRehab.setOnCheckedChangeListener((rgG, nChecked) -> {
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
        });

		
		// add button listener
		btnHelp.setOnClickListener(arg0 -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntC);

            // set title
            alertDialogBuilder.setTitle("Calculate Help");

            // set dialog message
            alertDialogBuilder.setMessage("Enter the address and square footage of the property, " +
                                          "including the number of bedrooms and bathrooms, sale price, ARV, budget items " +
                                          "and rehab budget. Rehab budget can be a flat rate or a rehab type. " +
                                          "Rehab types are classified as:  Low ($15/sf, yard work and painting), " +
                                          "Medium ($20/sf > 1500 sf or $25/sf < 1500 sf, Low + kitchen and bathrooms, " +
                                          "High ($30/sf, Medium + new roof), Super-High ($40/sf, complete gut job), " +
                                          "Bulldozer ($125/sf, demolition and rebuild).")
                              .setCancelable(false)
                              .setNeutralButton("OK", (dialog, id) -> {
                                  // if this button is clicked, close
                                  // current activity
                                  dialog.cancel();
                              });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        });
		
		// gets Intent and Calculate object
        // Intent object from ResultsActivity
        Intent intR = getIntent();
        // Calculate object from ResultsActivity
        Calculate calR = (Calculate) intR.getSerializableExtra("Calculate");
		// if Calculate object is null, fields are blank
		if (calR == null) {
			etAddress.setText("");
			etCityStZip.setText("");
			etSquareFootage.setText("");
			etBedrooms.setText("");
			etBathrooms.setText("");
			etSalesPrice.setText("");
			etFMVARV.setText("");
			etBudgetItems.setText("");
		} else {
			// set fields to member variables of Calculate object
			etAddress.setText(calR.getAddress());
			etCityStZip.setText(calR.getCityStZip());
			etSquareFootage.setText(calR.getSquareFootage());
			etBedrooms.setText(calR.getBedrooms());
			etBathrooms.setText(calR.getBathrooms() + "");
			etSalesPrice.setText((int) calR.getSalesPrice());
			etFMVARV.setText((int) calR.getFMVARV());
			etBudgetItems.setText(calR.getBudgetItems());
			if (calR.getRehabFlag() == 0) {
				  rbRehab1.setChecked(true);
				  rbRehab2.setChecked(false);
	        	  tvRehabFlatRate.setVisibility(View.VISIBLE);
	        	  etRehabBudget.setVisibility(View.VISIBLE);
	        	  tvRehabType.setVisibility(View.INVISIBLE);
	        	  spnRehabType.setVisibility(View.INVISIBLE);
	        	  etRehabBudget.setText((int) calR.getBudget());
			} else {
				  rbRehab1.setChecked(false);
				  rbRehab2.setChecked(true);
	        	  tvRehabFlatRate.setVisibility(View.INVISIBLE);
	        	  etRehabBudget.setVisibility(View.INVISIBLE);
	        	  tvRehabType.setVisibility(View.VISIBLE);
	        	  spnRehabType.setVisibility(View.VISIBLE);
	        	  int nCostSF = (int)(calR.getBudget()/ calR.getSquareFootage());
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
		if (etAddress.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter Address", Toast.LENGTH_SHORT).show();
		} else if (etCityStZip.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter City/State/Zip", Toast.LENGTH_SHORT).show();
		} else if (etSquareFootage.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter Square Footage", Toast.LENGTH_SHORT).show();
		} else if (etBedrooms.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter Bedrooms", Toast.LENGTH_SHORT).show();
		} else if (etBathrooms.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter Bathrooms", Toast.LENGTH_SHORT).show();
		} else if (etSalesPrice.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter Sales Price", Toast.LENGTH_SHORT).show();
		} else if (etFMVARV.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter Fair Market Value or After Repair Value", Toast.LENGTH_SHORT).show();
		} else if (etBudgetItems.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Must Enter Budget Items", Toast.LENGTH_SHORT).show();
		} else {
			Intent intI = new Intent(CalculateActivity.this, ResultsActivity.class);
	    
			// creates new Calculate object and sets info from fields into object variables
			Calculate calC = new Calculate();
			calC.setAddress(etAddress.getText().toString());
			calC.setCityStZip(etCityStZip.getText().toString());
			calC.setSquareFootage(Integer.parseInt(etSquareFootage.getText().toString()));
			calC.setBedrooms(Integer.parseInt(etBedrooms.getText().toString()));
			calC.setBathrooms(Double.parseDouble(etBathrooms.getText().toString()));
			calC.setFMVARV(Integer.parseInt(etFMVARV.getText().toString()));
			calC.setSalesPrice(Integer.parseInt(etSalesPrice.getText().toString()));
			calC.setBudgetItems(etBudgetItems.getText().toString());
			int nSelected = rgRehab.getCheckedRadioButtonId();
            // rehab button id
            RadioButton rbRehab = findViewById(nSelected);

            // determines Rehab object by radio button input
			if (nSelected == R.id.rdoRehabNumber) {
                if (etRehabBudget.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Must Enter Rehab Budget", Toast.LENGTH_SHORT).show();
                } else {
                    // budget
                    double dB = Double.parseDouble(etRehabBudget.getText().toString());
                    calC.setBudget(dB);
                }
                calC.setRehabFlag(0);
            } else {
                String strRTSel = spnRehabType.getSelectedItem().toString();
                calC.calcBudgetRehabType(strRTSel);
                calC.setRehabFlag(1);
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
         // do something when the button is clicked
         // do something when the button is clicked
         AlertDialog adAlertBox = new AlertDialog.Builder(this)
		    .setMessage("Do you want to go back to main menu?")
		    .setPositiveButton("Yes", (arg0, arg1) -> {
                Intent intB = new Intent(CalculateActivity.this, MainActivity.class);
                startActivity(intB);
                finish();
                //close();
            })
		    .setNegativeButton("No", (arg0, arg1) -> {
            })
		    .show();
	 }
}
