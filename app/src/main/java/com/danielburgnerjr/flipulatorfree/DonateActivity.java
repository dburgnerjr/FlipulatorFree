package com.danielburgnerjr.flipulatorfree;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DonateActivity extends Activity {
    private Spinner mGoogleSpinner;

    protected boolean mDebug = false;

    protected String mGooglePubkey = "";
    protected String[] mGoogleCatalog = new String[]{};
    protected String[] mGoogleCatalogValues = new String[]{};

    BillingClient billingClient;

    private ArrayList<String> productIds;

    List<QueryProductDetailsParams.Product> products;

    QueryProductDetailsParams queryProductDetailsParams;

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

    private void initializeBillingClient() {
        PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK &&
                    purchases != null) {
                for (Purchase purchase : purchases) {
                    // Process the purchase as described in the next section.
                    verifyPurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user canceling the purchase flow.
                Toast.makeText(getApplicationContext(), "purchase canceled", Toast.LENGTH_LONG).show();
            } else {
                // Handle any other error codes.
                Toast.makeText(getApplicationContext(), "purchase error other reasons", Toast.LENGTH_LONG).show();
            }
        };

        PendingPurchasesParams params = PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts().build();

        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases(params)
                .enableAutoServiceReconnection() // Add this line to enable reconnection
                .build();
    }

    private void connectToBillingSystem() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
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
        products = new ArrayList<>();
        for (String id : productIds) {
            products.add(QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(id)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build());
        }

        queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
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
        AtomicReference<ProductDetails> selectedProduct = new AtomicReference<>();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                (billingResult, queryProductDetailsResult) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (ProductDetails productDetails : queryProductDetailsResult.getProductDetailsList()) {
                            // Process success retrieved product details here.
                            if (productDetails.getProductId().equals(mGoogleCatalog[index])) {
                                selectedProduct.set(productDetails);
                                break;
                            }
                        }
                    }
                    ProductDetails selProd = selectedProduct.get();
                    startPurchaseFlow(selProd);
                }
        );
    }

    private void startPurchaseFlow(ProductDetails productDetails) {
        List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                List.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(productDetails)
                                // Get the offer token:
                                // a. For one-time products, call ProductDetails.getOneTimePurchaseOfferDetailsList()
                                // for a list of offers that are available to the user.
                                // b. For subscriptions, call ProductDetails.subscriptionOfferDetails()
                                // for a list of offers that are available to the user.
                                .setOfferToken(Objects.requireNonNull(Objects.requireNonNull(productDetails.getOneTimePurchaseOfferDetails()).getOfferToken()))
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        // Launch the billing flow
        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    private void verifyPurchase(Purchase purchase) {
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = (billingResult, purchaseToken) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
                Toast.makeText(getApplicationContext(), "Purchase successful", Toast.LENGTH_LONG).show();
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }

    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().
                setProductType(BillingClient.ProductType.INAPP).build(), (billingResult, list) -> {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            for (Purchase purchase : list) {
                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                                    // show toast message to user
                                    Toast.makeText(getApplicationContext(), "You have purchases pending", Toast.LENGTH_LONG).show();
                                } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED
                                        && !purchase.isAcknowledged()) {
                                    verifyPurchase(purchase);
                                }
                            }
                        }
                });
    }

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
}
