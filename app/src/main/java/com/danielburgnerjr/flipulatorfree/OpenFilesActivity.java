package com.danielburgnerjr.flipulatorfree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class OpenFilesActivity extends Activity {
	
	ListView lvView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openfiles);

		// get the files and place it in a file array
		File myDir = new File(getApplicationContext().getExternalFilesDir(null) + "/FlipulatorFree");
		String strPath = myDir.getPath();
		File fFileList = new File(strPath);
		File[] fFileArray = fFileList.listFiles();
		List<String> arrayList = new ArrayList<>();
		
		// Get ListView object from xml
        lvView = findViewById(R.id.filesList);
        
        // Defined Array values to show in ListView
        for (int nI = 0; nI < Objects.requireNonNull(fFileArray).length; nI++) {
        	arrayList.add(fFileArray[nI].getName().substring(0, fFileArray[nI].getName().length()-4));
        }
        
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Fourth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);


        // Assign adapter to ListView
        lvView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        lvView.setOnItemClickListener((parent, view, position, id) -> {

            // ListView Clicked item value
         String itemValue = (String) lvView.getItemAtPosition(position);

         // Get file and open it
         //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
         Intent newActivity;
         Calculate calC = null;
         try {
             File fPath = new File(getApplicationContext().getExternalFilesDir(null) + "/FlipulatorFree");
             String strFilePathXLS = fPath.getPath() + "/" + itemValue + ".xls";
             try {
                 Workbook wbExcelFile = Workbook.getWorkbook(new File(strFilePathXLS));
                 Sheet shWorkSheet = wbExcelFile.getSheet(0);

                 // Calculate
                 Cell celAddress = shWorkSheet.getCell(1, 1);
                 Cell celCityStZip = shWorkSheet.getCell(1, 2);
                 Cell celSqFootage = shWorkSheet.getCell(1, 4);
                 Cell celBedrooms = shWorkSheet.getCell(1, 5);
                 Cell celBathrooms = shWorkSheet.getCell(3, 5);
                 Cell celFMVARV = shWorkSheet.getCell(3, 7);
                 Cell celSalesPrice = shWorkSheet.getCell(3, 9);
                 Cell celBudget = shWorkSheet.getCell(3, 11);
                 Cell celBudgetItems = shWorkSheet.getCell(1, 21);
                 Cell celRehabType = shWorkSheet.getCell(3, 26);

                 String strAddress = celAddress.getContents();
                 String strCityStZip = celCityStZip.getContents();
                 int nSqFootage = Integer.parseInt(celSqFootage.getContents());
                 int nBedrooms = Integer.parseInt(celBedrooms.getContents());
                 double dBathrooms = Double.parseDouble(celBathrooms.getContents());
                 String strBudgetItems = celBudgetItems.getContents();

                 Locale us = new Locale("en", "US");
                 NumberFormat nbfDollar = NumberFormat.getCurrencyInstance(us);

                 Number numFMVARV = nbfDollar.parse(celFMVARV.getContents());
                 assert numFMVARV != null;
                 double dFMVARV = numFMVARV.doubleValue();
                 Number numSalesPrice = nbfDollar.parse(celSalesPrice.getContents());
                 assert numSalesPrice != null;
                 double dSalesPrice = numSalesPrice.doubleValue();
                 Number numRehabBudget = nbfDollar.parse(celBudget.getContents());
                 assert numRehabBudget != null;
                 double dRehabBudget = numRehabBudget.doubleValue();

                 int nRehabFlag = getRehabFlag(celRehabType);

                 calC = new Calculate();
                    calC.setAddress(strAddress);
                    calC.setCityStZip(strCityStZip);
                    calC.setSquareFootage(nSqFootage);
                    calC.setBedrooms(nBedrooms);
                    calC.setBathrooms(dBathrooms);
                    calC.setFMVARV(dFMVARV);
                    calC.setSalesPrice(dSalesPrice);
                    calC.setBudgetItems(strBudgetItems);
                    calC.setBudget(dRehabBudget);
                    calC.setRehabFlag(nRehabFlag);

             } catch (FileNotFoundException e) {
                 Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
             } catch (BiffException e) {
                 Toast.makeText(getApplicationContext(), "Biff Exception", Toast.LENGTH_SHORT).show();
             } catch (ParseException e) {
                 Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
             }
         } catch (IOException e) {
             Toast.makeText(getApplicationContext(), "IO Exception", Toast.LENGTH_SHORT).show();
         } finally {
             newActivity = new Intent(OpenFilesActivity.this, CalculateActivity.class);
             newActivity.putExtra("Calculate", calC);
             startActivity(newActivity);
             finish();
         }
      });

	}

    private static int getRehabFlag(Cell celRehabType) {
        String strRehabType = celRehabType.getContents();
        int nRehabFlag = -1;
        if (strRehabType.equals("Flat Rate")) {
            nRehabFlag = 0;
        }

        if ((strRehabType.equals("Low")) || (strRehabType.equals("Medium")) ||
              (strRehabType.equals("High")) || (strRehabType.equals("Super-High")) ||
              (strRehabType.equals("Bulldozer"))) {
            nRehabFlag = 1;
        }
        return nRehabFlag;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intI = new Intent(OpenFilesActivity.this, MainActivity.class);
		    startActivity(intI);
		    finish();
		    return true;
		}
		return super.onKeyDown(keyCode, event);
   }
}
