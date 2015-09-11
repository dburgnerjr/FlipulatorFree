package com.danielburgnerjr.flipulatorfree;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;

import com.danielburgnerjr.flipulatorfree.util.IabHelper;
import com.danielburgnerjr.flipulatorfree.util.IabResult;
import com.danielburgnerjr.flipulatorfree.util.Purchase;

public class DonateFragment extends Fragment {

    public static final String ARG_DEBUG = "debug";

    public static final String ARG_GOOGLE_ENABLED = "googleEnabled";
    public static final String ARG_GOOGLE_PUBKEY = "googlePubkey";
    public static final String ARG_GOOGLE_CATALOG = "googleCatalog";
    public static final String ARG_GOOGLE_CATALOG_VALUES = "googleCatalogValues";

    private static final String TAG = "Donations Library";

    // http://developer.android.com/google/play/billing/billing_testing.html
    private static final String[] CATALOG_DEBUG = new String[]{"android.test.purchased",
            "android.test.canceled", "android.test.refunded", "android.test.item_unavailable"};

    private EditText mGoogleEditText;
    private Spinner mGoogleSpinner;
    
    // Google Play helper object
    private IabHelper mHelper;

    protected boolean mDebug = false;

    protected boolean mGoogleEnabled = false;
    protected String mGooglePubkey = "";
    protected String[] mGoogleCatalog = new String[]{};
    protected String[] mGoogleCatalogValues = new String[]{};

    /**
     * Instantiate DonationsFragment.
     *
     * @param debug               You can use BuildConfig.DEBUG to propagate the debug flag from your app to the Donations library
     * @param googleEnabled       Enabled Google Play donations
     * @param googlePubkey        Your Google Play public key
     * @param googleCatalog       Possible item names that can be purchased from Google Play
     * @param googleCatalogValues Values for the names
     * @return DonationsFragment
     */
    public static DonateFragment newInstance(boolean debug, boolean googleEnabled, String googlePubkey, String[] googleCatalog,
                                                String[] googleCatalogValues) {
        DonateFragment donateFragment = new DonateFragment();
        Bundle args = new Bundle();

        args.putBoolean(ARG_DEBUG, debug);

        args.putBoolean(ARG_GOOGLE_ENABLED, googleEnabled);
        args.putString(ARG_GOOGLE_PUBKEY, googlePubkey);
        args.putStringArray(ARG_GOOGLE_CATALOG, googleCatalog);
        args.putStringArray(ARG_GOOGLE_CATALOG_VALUES, googleCatalogValues);

        donateFragment.setArguments(args);
        return donateFragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDebug = getArguments().getBoolean(ARG_DEBUG);

        mGoogleEnabled = getArguments().getBoolean(ARG_GOOGLE_ENABLED);
        mGooglePubkey = getArguments().getString(ARG_GOOGLE_PUBKEY);
        mGoogleCatalog = getArguments().getStringArray(ARG_GOOGLE_CATALOG);
        mGoogleCatalogValues = getArguments().getStringArray(ARG_GOOGLE_CATALOG_VALUES);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // choose donation amount
    	View viewV = (ViewGroup) inflater.inflate(R.layout.donatefraggoogle, container, false);
        mGoogleEditText = (EditText) viewV.findViewById(R.id.edtDonate);
        return viewV;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Google */
        if (mGoogleEnabled) {
            // inflate google view into stub
            ViewStub googleViewStub = (ViewStub) getActivity().findViewById(R.id.vwsGoogle);
            googleViewStub.inflate();
            
            Button btGoogle = (Button) getActivity().findViewById(R.id.donations__google_android_market_donate_button);
            btGoogle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    donateGoogleOnClick(v);
                }
            });

            // Create the helper, passing it our context and the public key to verify signatures with
            if (mDebug)
                Log.d(TAG, "Creating IAB helper.");
            mHelper = new IabHelper(getActivity(), mGooglePubkey);

            // enable debug logging (for a production application, you should set this to false).
            mHelper.enableDebugLogging(mDebug);

            // Start setup. This is asynchronous and the specified listener
            // will be called once setup completes.
            if (mDebug)
                Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (mDebug)
                        Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        openDialog(android.R.drawable.ic_dialog_alert, R.string.donations__google_android_market_not_supported_title,
                                getString(R.string.donations__google_android_market_not_supported));
                        return;
                    }

                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null) return;
                }
            });
        }
    }

    /**
     * Open dialog
     */
    void openDialog(int icon, int title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setIcon(icon);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setNeutralButton(R.string.donations__button_close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        dialog.show();
    }

    /**
     * Donate button executes donations based on selection in spinner
     */
    public void donateGoogleOnClick(View view) {
        String strDonation;
        strDonation = mGoogleEditText.getText().toString();
        if (mDebug)
            Log.d(TAG, "selected item in spinner: " + strDonation);

        if (mDebug) {
            // when debugging, choose android.test.x item
            mHelper.launchPurchaseFlow(getActivity(),
            		strDonation, IabHelper.ITEM_TYPE_INAPP,
                    0, mPurchaseFinishedListener, null);
        } else {
            mHelper.launchPurchaseFlow(getActivity(),
            		strDonation, IabHelper.ITEM_TYPE_INAPP,
                    0, mPurchaseFinishedListener, null);
        }
    }

    // Callback for when a purchase is finished
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

    // Called when consumption is complete
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

    @Override
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
}
