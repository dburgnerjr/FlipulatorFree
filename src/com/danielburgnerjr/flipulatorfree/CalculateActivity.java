package com.danielburgnerjr.flipulatorfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

public class CalculateActivity extends Activity {
	
	final Context cntC = this;
	
	private EditText etAddress;			// address
	private EditText etCityStZip;	    // city state zip code
	private EditText etSquareFootage;	// square footage
	private EditText etBedrooms;		// number of bedrooms
	private EditText etBathrooms;		// number of bathrooms
	private EditText etSalesPrice;		// sales price
	private EditText etFMVARV;			// fair mkt value/after repair value
	private RadioGroup rgRehab;			// radio group rehab
	private RadioButton rbRehab;		// rehab radio buttons
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

	          if (nChecked==R.id.rdoRehabNumber) {
	        	  tvRehabFlatRate.setVisibility(View.VISIBLE);
	        	  etRehabBudget.setVisibility(View.VISIBLE);
	        	  tvRehabType.setVisibility(View.INVISIBLE);
	        	  spnRehabType.setVisibility(View.INVISIBLE);
	          } else if (nChecked==R.id.rdoRehabType) {
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
											  "Medium ($20/sf > 1500 sf or $25/sf < 1500 sf, Low + kitchen and bathrooms," +
											  "High ($30/sf, Medium + new roof), Super-High ($40/sf, complete gut job)," +
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

	}

	public void nextPage(View view) {
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
											break;
											
				case R.id.rdoRehabType:
											String strRTSel = spnRehabType.getSelectedItem().toString();
											calC.calcBudgetRehabType(strRTSel);
											break;
											
		}
	    
			intI.putExtra("Calculate", calC);	    
			startActivity(intI);
		}
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
