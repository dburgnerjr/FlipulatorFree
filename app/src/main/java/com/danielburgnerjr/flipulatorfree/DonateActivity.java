package com.danielburgnerjr.flipulatorfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.android.billingclient.api.UnfetchedProduct;

import java.util.ArrayList;
import java.util.List;

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

    private BillingClient billingClient;

    private ArrayList<String> productIds;


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

        // Cash App title
        TextView txtCashAppTitle = findViewById(R.id.txtCashAppTitle);
        // Cash App description
        TextView txtCashAppDesc = findViewById(R.id.txtCashAppDescription);

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
        txtPayPalTitle.setVisibility(View.VISIBLE);
        txtPayPalDesc.setVisibility(View.VISIBLE);
        btnPayPal.setVisibility(View.VISIBLE);
        
        btnPayPal.setOnClickListener(this::donatePayPalOnClick);

        // donate cash app
        Button btnCashApp = findViewById(R.id.btnDonateCashApp);

        // set PayPal invisible for Google Play
        txtCashAppTitle.setVisibility(View.VISIBLE);
        txtCashAppDesc.setVisibility(View.VISIBLE);
        btnCashApp.setVisibility(View.VISIBLE);

        btnCashApp.setOnClickListener(this::donateCashAppOnClick);

        initializeBillingClient();
        connectToBillingSystem();
        productIds = loadProductIds();
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

    private void initializeBillingClient() {
        Log.i(TAG, "Inside initializeBillingClient...");

        PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
            // To be implemented in a later section.
        };

        PendingPurchasesParams params = PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts().build();

        BillingClient billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases(params)
                .enableAutoServiceReconnection() // Add this line to enable reconnection
                .build();
    }

    private void connectToBillingSystem() {
        Log.i(TAG, "Inside connectToBillingSystem...");

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.i(TAG, "Connection Success.");
                    queryProductDetails();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void queryProductDetails() {
        List<QueryProductDetailsParams.Product> products = new ArrayList<>();
        for (String id : productIds) {
            products.add(QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(id)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build());
        }

        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(products)
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                (billingResult, queryProductDetailsResult) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (ProductDetails productDetails : queryProductDetailsResult.getProductDetailsList()) {
                            // Process success retrieved product details here.
                            Log.i("queryProductDetails: ", productDetails + "");
                        }

                        for (UnfetchedProduct unfetchedProduct : queryProductDetailsResult.getUnfetchedProductList()) {
                            // Handle any unfetched products as appropriate.
                        }
                    }
                }
        );
    }

    private ArrayList<String> loadProductIds() {
        ArrayList<String> ids = new ArrayList<>();
        ids.add("donation1");
        ids.add("donation5");
        ids.add("donation10");
        ids.add("donation25");
        ids.add("donation50");
        ids.add("donation100");

        return ids;
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
     * Donate Cash App button executes link to Cash App donation page
     */
    public void donateCashAppOnClick(View view) {
        String strCashApp = "http://cash.app/$dburgnerjr";
        Intent newActivity = new Intent(Intent.ACTION_VIEW,  Uri.parse(strCashApp));
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
