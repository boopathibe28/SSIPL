package com.traceabilitysystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.traceabilitysystem.utils.CommonFunctions;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private String order_key = "";
    private String order_product_id = "";
    private String product_qty = "";
    private String product_name = "";
    private int tempQty = 0;
    private String backpress = "";;

    //camera permission is needed.
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view


    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        // Do something with the result here
        Log.v("kkkk", result.getContents()); // Prints scan results
        Log.v("uuuu", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        Toast.makeText(ScanActivity.this,result+"", Toast.LENGTH_LONG).show();

        Bundle bundle = new Bundle();
        bundle.putString("from","scaning");
        bundle.putString("barCode",result.getContents().replace("http://",""));
        //CommonFunctions.getInstance().newIntent(ScanActivity.this, LookingForSomething.class, bundle, true,false);

    }


}