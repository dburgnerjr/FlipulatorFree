package com.danielburgnerjr.flipulatorfree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Alignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
	private WritableCellFormat timesBold;
	private WritableCellFormat times;

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
	public void editInfo(View view) {
		Intent intI = new Intent(ResultsActivity.this, CalculateActivity.class);
		intI.putExtra("Calculate", calC);
		startActivity(intI);
		finish();
	}

	@SuppressWarnings("deprecation")
	public void createSpreadsheet(File fDir, String strXLSFile) throws IOException, WriteException {
		File fileXls = new File(fDir, strXLSFile);
	    WorkbookSettings wbSettings = new WorkbookSettings();

	    wbSettings.setLocale(new Locale("en", "EN"));

	    WritableWorkbook workbook = Workbook.createWorkbook(fileXls, wbSettings);
	    workbook.createSheet(calC.getAddress() + " " + calC.getCityStZip(), 0);
	    WritableSheet excelSheet = workbook.getSheet(0);
	    StringBuffer buf = new StringBuffer();

	    // Lets create a times font
	    WritableFont times18ptHeader = new WritableFont(WritableFont.ARIAL, 18, WritableFont.BOLD);
	    // Define the cell format
	    times = new WritableCellFormat(times18ptHeader);
	    // Lets automatically wrap the cells
	    times.setWrap(true);

	    // create a bold font
	    WritableFont times10ptBold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
	    timesBold = new WritableCellFormat(times10ptBold);
	    // Lets automatically wrap the cells
	    timesBold.setWrap(true);

	    CellView cv = new CellView();
	    cv.setFormat(times);
	    cv.setFormat(timesBold);
	    cv.setAutosize(true);

	    // Number and writable cell formats
	    NumberFormat nbfDollar = new NumberFormat("$###,##0");
	    WritableCellFormat wcfDollar = new WritableCellFormat(nbfDollar);
	    WritableCellFormat wcfProfit = new WritableCellFormat(times10ptBold, nbfDollar);

	    NumberFormat nbfPercent = new NumberFormat("##.0%");
	    WritableCellFormat wcfPercent = new WritableCellFormat(times10ptBold, nbfPercent);

	    NumberFormat nbfPercentTwoPlaces = new NumberFormat("#0.00%");
	    WritableCellFormat wcfPercentTwoPlaces = new WritableCellFormat(nbfPercentTwoPlaces);

	    // Write a few headers
	    WritableCell wrcResults = new Label(0, 0, "Results", times);
	    excelSheet.addCell(wrcResults);
	    excelSheet.mergeCells(0, 0, 4, 0);
	    
	    // set up column and row view
	    excelSheet.setColumnView(0, 25);
	    excelSheet.setRowView(0, (int)((1.5d * 14)*20), false);

	    // location info - original
	    Label lblPropAddressOrig;
	    lblPropAddressOrig = new Label(0, 1, "Property Address:", timesBold);
	    excelSheet.addCell(lblPropAddressOrig);
	    excelSheet.mergeCells(1, 1, 4, 1);
	    Label lblStreetAddress = new Label(1, 1, calC.getAddress(), timesBold);
	    excelSheet.addCell(lblStreetAddress);

	    Label lblCityOrig;
	    lblCityOrig = new Label(0, 2, "City, State ZIP:", timesBold);
	    excelSheet.addCell(lblCityOrig);
	    excelSheet.mergeCells(1, 2, 4, 1);
	    Label lblCity = new Label(1, 2, calC.getCityStZip(), timesBold);
	    excelSheet.addCell(lblCity);

	    Label lblSquareFootageOrig;
	    lblSquareFootageOrig = new Label(0, 4, "Square Footage:", timesBold);
	    excelSheet.addCell(lblSquareFootageOrig);
	    excelSheet.mergeCells(1, 4, 4, 1);
	    Label lblSquareFootage = new Label(1, 4, calC.getSquareFootage() + "", timesBold);
	    excelSheet.addCell(lblSquareFootage);

	    Label lblBROrig;
	    lblBROrig = new Label(0, 5, "BR:", timesBold);
	    excelSheet.addCell(lblBROrig);
	    Label lblBR = new Label(1, 5, calC.getBedrooms() + "", timesBold);
	    excelSheet.addCell(lblBR);

	    Label lblBAOrig;
	    lblBAOrig = new Label(2, 5, "BA:", timesBold);
	    excelSheet.addCell(lblBAOrig);
	    Label lblBA = new Label(3, 5, calC.getBathrooms() + "", timesBold);
	    excelSheet.addCell(lblBA);

	    // results info
	    Label lblFMVARVOriginal;
	    lblFMVARVOriginal = new Label(0, 7, "FMV/ARV:", timesBold);
	    excelSheet.addCell(lblFMVARVOriginal);
	    Number nbrFMVARVSixMonthO = new Number(3, 7, calC.getFMVARV(), wcfDollar);
	    excelSheet.addCell(nbrFMVARVSixMonthO);

	    Label lblSalesPriceOrig;
	    lblSalesPriceOrig = new Label(0, 9, "Sales Price:", timesBold);
	    excelSheet.addCell(lblSalesPriceOrig);
	    Number nbrSalesPriceO = new Number(3, 9, calC.getSalesPrice(), wcfDollar);
	    excelSheet.addCell(nbrSalesPriceO);

	    Label lblRehabOrig;
	    lblRehabOrig = new Label(0, 11, "Rehab Budget:", timesBold);
	    excelSheet.addCell(lblRehabOrig);
	    Number nbrRehabO = new Number(3, 11, calC.getBudget(), wcfDollar);
	    excelSheet.addCell(nbrRehabO);

	    Label lblClosHoldCosts;
	    lblClosHoldCosts = new Label(0, 13, "Clos/Hold Costs:", timesBold);
	    excelSheet.addCell(lblClosHoldCosts);

	    buf = new StringBuffer();
	    buf.append("(D8*0.1)");
	    Formula forClosHoldCosts = new Formula(3, 13, buf.toString(), wcfDollar);
	    excelSheet.addCell(forClosHoldCosts);

	    Label lblNetProfitOriginal;
	    lblNetProfitOriginal = new Label(0, 15, "Net Profit:", timesBold);
	    excelSheet.addCell(lblNetProfitOriginal);

	    buf = new StringBuffer();
	    buf.append("(D8-D10-D12-D14)");
	    Formula forNetProfitSixMonthO = new Formula(3, 15, buf.toString(), wcfProfit);
	    excelSheet.addCell(forNetProfitSixMonthO);

	    Label lblRateOfReturnOriginal;
	    lblRateOfReturnOriginal = new Label(0, 17, "Rate of Return:", timesBold);
	    excelSheet.addCell(lblRateOfReturnOriginal);

	    buf = new StringBuffer();
	    buf.append("(D16/D8)");
	    Formula forRateOfReturnSixMonthO = new Formula(3, 17, buf.toString(), wcfPercent);
	    excelSheet.addCell(forRateOfReturnSixMonthO);

	    Label lblCashCashReturnOriginal;
	    lblCashCashReturnOriginal = new Label(0, 19, "Cash on Cash Return:", timesBold);
	    excelSheet.addCell(lblCashCashReturnOriginal);

	    buf = new StringBuffer();
	    buf.append("(D16/(D12+D14))");
	    Formula forCashCashReturnSixMonthO = new Formula(3, 19, buf.toString(), wcfPercent);
	    excelSheet.addCell(forCashCashReturnSixMonthO);

	    // budget items
	    Label lblBudgetItems;
	    lblBudgetItems = new Label(0, 21, "Budget Items:", timesBold);
	    excelSheet.addCell(lblBudgetItems);
	    Label lblBudgetItemsList = new Label(1, 21, calC.getBudgetItems(), timesBold);
	    excelSheet.addCell(lblBudgetItemsList);
	    excelSheet.mergeCells(1, 21, 5, 24);
	    
	    // rehab type
	    Label lblRehabType;
	    lblRehabType = new Label(0, 26, "Budget Based On:", timesBold);
	    String strRehabType = "";
	    excelSheet.addCell(lblRehabType);
	    if (calC.getRehabFlag() == 0) {
	    	strRehabType = "Flat Rate";
	    }
	    if (calC.getRehabFlag() == 1) {
	    	int nPriceSqFt = ((int)calC.getBudget()/calC.getSquareFootage());
	    	switch (nPriceSqFt) {
  	  			case 15:
  	  						strRehabType = "Low";
  	  						break;
  	  			case 20:
  	  			case 25:
  	  						strRehabType = "Medium";
  	  						break;
  	  			case 30:
  	  						strRehabType = "High";
  	  						break;
  	  			case 40:
  	  						strRehabType = "Super-High";
  	  						break;
  	  			case 125:
  	  						strRehabType = "Bulldozer";
  	  						break;
	    	}
	    }
	    Label lblRehabTypeValue = new Label(3, 26, strRehabType, timesBold);
	    excelSheet.addCell(lblRehabTypeValue);
	    
	    workbook.write();
	    workbook.close();		
	}

	public void nextPage(View view) {
		AlertDialog adAlertBox = new AlertDialog.Builder(this)
	    .setMessage("Do you want to email results as text or as an Excel spreadsheet?")
	    .setPositiveButton("Text", new DialogInterface.OnClickListener() {
	        // do something when the button is clicked
	        public void onClick(DialogInterface arg0, int arg1) {
	            emailPlainText();
	            //close();
	        }
	    })
	    .setNegativeButton("Excel", new DialogInterface.OnClickListener() {
	        // do something when the button is clicked
	        public void onClick(DialogInterface arg0, int arg1) {
	        	try {
	        		emailExcelSpreadsheet();
	        	} catch (IOException e) {
	        		Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
	        	} catch (WriteException e) {
	        		Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
	        	}
	        }
	    })
	    .show();
	}

	public void emailExcelSpreadsheet() throws IOException, WriteException {
		File myDir = new File(getApplicationContext().getExternalFilesDir(null) + "/FlipulatorFree");
	    myDir.mkdirs();
	    String strFileNameXls = calC.getAddress() + " " + calC.getCityStZip() + ".xls";
		File file = new File(myDir, strFileNameXls);
		
		if (!file.exists()) {
			createSpreadsheet(myDir, strFileNameXls);
		}
		
		Intent intEmailActivity = new Intent(Intent.ACTION_SEND);
		intEmailActivity.putExtra(Intent.EXTRA_EMAIL, new String[]{});
		intEmailActivity.putExtra(Intent.EXTRA_SUBJECT, "Flipulator Free results for: " + calC.getAddress() + " " + calC.getCityStZip());
		intEmailActivity.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+  file));
		intEmailActivity.setType("application/excel");
   		startActivity(intEmailActivity);

	}

	public void emailPlainText() {
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

	public void saveFile(View view) throws FileNotFoundException, IOException, WriteException {
		// saves results to text file
		File myDir = new File(getApplicationContext().getExternalFilesDir(null) + "/FlipulatorFree");
	    myDir.mkdirs();
	    String strFileNameXls = calC.getAddress() + " " + calC.getCityStZip() + ".xls";
		File file = new File(myDir, strFileNameXls);
		
		// create Excel spreadsheet
		createSpreadsheet(myDir, strFileNameXls);
		
		String strSavedFile = "File saved as: " + strFileNameXls;
		Toast.makeText(getApplicationContext(), strSavedFile, Toast.LENGTH_SHORT).show();
	}
	
	public boolean onKeyDown(int nKeyCode, KeyEvent keEvent) {
		String strBackMessage = "Press Save to save info to file, Edit to make changes, Main Menu to return to main menu ";
		strBackMessage += " or Email Results to email.";
		if (nKeyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(getApplicationContext(), strBackMessage, Toast.LENGTH_SHORT).show();
		    return true;
		}
		return super.onKeyDown(nKeyCode, keEvent);
    }
}
