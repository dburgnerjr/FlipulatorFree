package com.danielburgnerjr.flipulatorfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

//import com.danielburgnerjr.flipulatorfree.util.IabHelper;
//import com.danielburgnerjr.flipulatorfree.util.IabResult;
//import com.danielburgnerjr.flipulatorfree.util.Purchase;

public class DonateActivity extends Activity {

    private Spinner mGoogleSpinner;

    // Google Play helper object
    //private IabHelper mHelper;

    protected boolean mDebug = false;

    protected boolean mGoogleEnabled = false;
    protected String mGooglePubkey = "";
    protected String[] mGoogleCatalog = new String[]{};
    protected String[] mGoogleCatalogValues = new String[]{};

    protected boolean mPaypalEnabled = false;
    protected String mPaypalUser = "";
    protected String mPaypalCurrencyCode = "";
    protected String mPaypalItemName = "";

    private static final String TAG = "Donations Library";

    private static final String[] CATALOG_DEBUG = new String[]{"android.test.purchased",
        "android.test.canceled", "android.test.refunded", "android.test.item_unavailable"};
    private static final String[] GOOGLE_CATALOG = new String[]{"donation1",
        "donation5", "donation10", "donation25", "donation50", "donation100"};
    /**
     * Google
     */
    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhUoMs4c4RcEIRckCir+wNCDZk8Zy3I4w7UjrPvKWIh8tl71HrEE+6BQiBCxseGfpuVEZntaHhzyQj5gMLSI5JBRboOlpItj7SyvupoHszsSh28VdJQiD3AWXB1LNeS9Z6W9RffSKYfEKs8v+dMwqzi+C2M+fPg9o7IcAXsCnJrVqS+vIhYrfyVX4oG3DQ28wfcWVPgGnNLP82y5VaP+xlJYdBZBQJrDBlQq1QaecSiR+wRG8ZBMv5V1x6w/QM4yKhoolz2Pc6Zt8YVkCVpQhf8usGcAy0d6ysW5YlkhFz2PbVoi553OkH3T5lZ5LO3cFXYG0g1ttsfE/8WiPYRlGxwIDAQAB";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donate);
		
		mGooglePubkey = GOOGLE_PUBKEY;
		mGoogleCatalog = GOOGLE_CATALOG;
		mGoogleCatalogValues = getResources().getStringArray(R.array.donation_google_catalog_values);

        // Paypal title
        TextView txtPayPalTitle = findViewById(R.id.txtPaypalTitle);
        // Paypal description
        TextView txtPayPalDesc = findViewById(R.id.txtPaypalDescription);

        // choose donation amount
		mGoogleSpinner = findViewById(R.id.spnDonate);
        ArrayAdapter<CharSequence> adapter;
        if (mDebug) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CATALOG_DEBUG);
        } else {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mGoogleCatalogValues);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGoogleSpinner.setAdapter(adapter);
        // donate google
        Button btnDonateNow = findViewById(R.id.btnDonateNow);
		
        btnDonateNow.setOnClickListener(this::donateGoogleOnClick);

        // Create the helper, passing it our context and the public key to verify signatures with
        if (mDebug)
            Log.d(TAG, "Creating IAB helper.");
        //mHelper = new IabHelper(this, mGooglePubkey);

        // enable debug logging (for a production application, you should set this to false).
        //mHelper.enableDebugLogging(mDebug);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        if (mDebug)
            Log.d(TAG, "Starting setup.");
/*
        mHelper.startSetup(result -> {
            if (mDebug)
                Log.d(TAG, "Setup finished.");

            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                openDialog(android.R.drawable.ic_dialog_alert, R.string.donations__google_android_market_not_supported_title,
                        getString(R.string.donations__google_android_market_not_supported));
            }
        });
*/

        // donate paypal
        Button btnPayPal = findViewById(R.id.btnDonatePaypal);

        // set PayPal invisible for Google Play
        txtPayPalTitle.setVisibility(View.INVISIBLE);
        txtPayPalDesc.setVisibility(View.INVISIBLE);
        btnPayPal.setVisibility(View.INVISIBLE);
        
        btnPayPal.setOnClickListener(this::donatePayPalOnClick);

	}

    /**
     * Open dialog
     */
    void openDialog(int icon, int title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(icon);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setNeutralButton(R.string.donations__button_close,
                (dialog1, which) -> dialog1.dismiss()
        );
        dialog.show();
    }

    /**
     * Donate button executes donations based on selection in spinner
     */
    public void donateGoogleOnClick(View view) {
        final int index;
        index = mGoogleSpinner.getSelectedItemPosition();
        if (mDebug)
            Log.d(TAG, "selected item in spinner: " + index);

/*
        if (mDebug) {
            // when debugging, choose android.test.x item
        	//Toast.makeText(getApplicationContext(), CATALOG_DEBUG[index], Toast.LENGTH_LONG).show();
            mHelper.launchPurchaseFlow(this,
            		CATALOG_DEBUG[index], IabHelper.ITEM_TYPE_INAPP,
                    0, mPurchaseFinishedListener, null);
        } else {
        	//Toast.makeText(getApplicationContext(), mGoogleCatalog[index], Toast.LENGTH_LONG).show();
            mHelper.launchPurchaseFlow(this,
            		mGoogleCatalog[index], IabHelper.ITEM_TYPE_INAPP,
                    0, mPurchaseFinishedListener, null);
        }
*/
    }

    // Callback for when a purchase is finished
/*
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (mDebug)
                Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isSuccess()) {
                if (mDebug)
                    Log.d(TAG, "Purchase successful.");

                // directly consume in-app purchase, so that people can donate multiple times
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);

                // show thanks openDialog
                openDialog(android.R.drawable.ic_dialog_info, R.string.donations__thanks_dialog_title,
                        getString(R.string.donations__thanks_dialog));
            }
        }
    };
*/

    // Called when consumption is complete
/*
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (mDebug)
                Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isSuccess()) {
                if (mDebug)
                    Log.d(TAG, "Consumption successful. Provisioning.");
            }
            if (mDebug)
                Log.d(TAG, "End consumption flow.");
        }
    };
*/

    /**
     * Donate Paypal button executes link to Paypal donation page
     */
    public void donatePayPalOnClick(View view) {
	    String strPaypal = "http://www.paypal.me/dburgnerjr";
   		Intent newActivity = new Intent(Intent.ACTION_VIEW,  Uri.parse(strPaypal));     
        startActivity(newActivity);

    }

    /**
     * Needed for Google Play In-app Billing. It uses startIntentSenderForResult(). The result is not propagated to
     * the Fragment like in startActivityForResult(). Thus we need to propagate manually to our Fragment.
     */
    //@Override
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mDebug)
            Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the fragment result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            if (mDebug)
                Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
*/
}
