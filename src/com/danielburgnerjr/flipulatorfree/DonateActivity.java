package com.danielburgnerjr.flipulatorfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.danielburgnerjr.flipulatorfree.DonateFragment;

public class DonateActivity extends FragmentActivity {

    /**
     * Google
     */
    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhUoMs4c4RcEIRckCir+wNCDZk8Zy3I4w7UjrPvKWIh8tl71HrEE+6BQiBCxseGfpuVEZntaHhzyQj5gMLSI5JBRboOlpItj7SyvupoHszsSh28VdJQiD3AWXB1LNeS9Z6W9RffSKYfEKs8v+dMwqzi+C2M+fPg9o7IcAXsCnJrVqS+vIhYrfyVX4oG3DQ28wfcWVPgGnNLP82y5VaP+xlJYdBZBQJrDBlQq1QaecSiR+wRG8ZBMv5V1x6w/QM4yKhoolz2Pc6Zt8YVkCVpQhf8usGcAy0d6ysW5YlkhFz2PbVoi553OkH3T5lZ5LO3cFXYG0g1ttsfE/8WiPYRlGxwIDAQAB";
    private static final String[] GOOGLE_CATALOG = new String[]{"ntpsync.donation.1",
            "ntpsync.donation.5", "ntpsync.donation.10", "ntpsync.donation.25", "ntpsync.donation.50",
            "ntpsync.donation.100"};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donate);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DonateFragment donateFragment;
        donateFragment = DonateFragment.newInstance(false, true, GOOGLE_PUBKEY, GOOGLE_CATALOG,
                    getResources().getStringArray(R.array.donation_google_catalog_values));

        ft.replace(R.id.DonateActivity, donateFragment, "donateFragment");
        ft.commit();
	}

    /**
     * Needed for Google Play In-app Billing. It uses startIntentSenderForResult(). The result is not propagated to
     * the Fragment like in startActivityForResult(). Thus we need to propagate manually to our Fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("donateFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
