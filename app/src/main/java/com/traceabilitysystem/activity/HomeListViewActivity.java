package com.traceabilitysystem.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.traceabilitysystem.R;
import com.traceabilitysystem.databinding.ActivityHomeListBinding;
import com.traceabilitysystem.interfaces.OnHomePressedListener;
import com.traceabilitysystem.utils.CommonFunctions;

import java.util.Objects;

import hari.bounceview.BounceView;

public class HomeListViewActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityHomeListBinding binding;
    private boolean doubleBackToExitPressedOnce = false;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_list);
        qrScan = new IntentIntegrator(HomeListViewActivity.this);
        checkPermission();

        initialView();


        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
              //  Toast.makeText(HomeListViewActivity.this, "Home Button Pressed", Toast.LENGTH_SHORT).show();
                // do something here...
            }
            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();

    }

    private void initialView() {
        BounceView.addAnimTo(binding.lyoutRMInventory);
        BounceView.addAnimTo(binding.lyoutRmStorage);
        BounceView.addAnimTo(binding.lyoutHRSlitting);
        BounceView.addAnimTo(binding.lyoutCrSlitting);
        BounceView.addAnimTo(binding.lyoutNarrowWidthCutToolLength);
        BounceView.addAnimTo(binding.lyoutOSLCutToolLength);
        BounceView.addAnimTo(binding.lyoutPacking);
        BounceView.addAnimTo(binding.lyoutFGStorage);
        BounceView.addAnimTo(binding.lyoutGRNProcess);
        BounceView.addAnimTo(binding.lyoutWeightCorrection);
        BounceView.addAnimTo(binding.lyoutSalesReturn);
        BounceView.addAnimTo(binding.lyoutFGDispatch);
        BounceView.addAnimTo(binding.lyoutRePrint);
        BounceView.addAnimTo(binding.lyoutWIPStore);
        BounceView.addAnimTo(binding.lyoutPrint);
        BounceView.addAnimTo(binding.lyoutOPBatchCorrection);
        BounceView.addAnimTo(binding.lyoutNWS);
        BounceView.addAnimTo(binding.lyoutNWCT);

        binding.lyoutRMInventory.setOnClickListener(this);
        binding.lyoutRmStorage.setOnClickListener(this);
        binding.lyoutHRSlitting.setOnClickListener(this);
        binding.lyoutCrSlitting.setOnClickListener(this);
        binding.lyoutNarrowWidthCutToolLength.setOnClickListener(this);
        binding.lyoutOSLCutToolLength.setOnClickListener(this);
        binding.lyoutPacking.setOnClickListener(this);
        binding.lyoutFGStorage.setOnClickListener(this);
        binding.lyoutFGDispatch.setOnClickListener(this);
        binding.lyoutGRNProcess.setOnClickListener(this);
        binding.lyoutWeightCorrection.setOnClickListener(this);
        binding.lyoutSalesReturn.setOnClickListener(this);
        binding.lyoutRePrint.setOnClickListener(this);
        binding.lyoutWIPStore.setOnClickListener(this);
        binding.lyoutPrint.setOnClickListener(this);
        binding.lyoutOPBatchCorrection.setOnClickListener(this);
        binding.lyoutNWS.setOnClickListener(this);
        binding.lyoutNWCT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.lyoutRMInventory){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, RMInventoryActivity.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutNWS){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, NWSActivity.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutNWCT){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, NWCTActivity.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutRmStorage){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, RMGridStorage.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutHRSlitting){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, HRSlitting.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutCrSlitting){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, CRSlitting.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutNarrowWidthCutToolLength){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, HRCutter.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutOSLCutToolLength){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, CRCutter.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutPacking){
           // CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, PackingConfirmation.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutFGStorage){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, FGStorage.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutFGDispatch){
           // CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, FGDispatch.class, Bundle.EMPTY, false, false);
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, FGDispatchNew.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutGRNProcess){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, GRNProcess.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutOPBatchCorrection){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, OPBatchCorrection.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutWeightCorrection){
            final Dialog dialog = new Dialog(HomeListViewActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_password);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.setCancelable(true);

            EditText edtPassword =  dialog.findViewById(R.id.edtPassword);
            Button btnOk =  dialog.findViewById(R.id.btnOk);
            Button btnCancel =  dialog.findViewById(R.id.btnCancel);

            dialog.show();

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtPassword.getText().toString().equals("739146")){
                        dialog.dismiss();
                        CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, WeightCorrection.class, Bundle.EMPTY, false, false);
                    }
                    else {
                        CommonFunctions.getInstance().validationError(HomeListViewActivity.this,"Enter Valid Password");
                    }
                }
            });
        }
        else if (view == binding.lyoutSalesReturn){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, SalesReturn.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutWIPStore){
            CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, WIPStoreActivity.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.lyoutRePrint){
            final Dialog dialog = new Dialog(HomeListViewActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_password);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.setCancelable(true);

            EditText edtPassword =  dialog.findViewById(R.id.edtPassword);
            Button btnOk =  dialog.findViewById(R.id.btnOk);
            Button btnCancel =  dialog.findViewById(R.id.btnCancel);

            dialog.show();

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtPassword.getText().toString().equals("4545")){
                        dialog.dismiss();
                        CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, Reprint.class, Bundle.EMPTY, false, false);
                    }
                    else {
                        CommonFunctions.getInstance().validationError(HomeListViewActivity.this,"Enter Valid Password");
                    }
                }
            });

        }
        else if (view == binding.lyoutPrint){
            final Dialog dialog = new Dialog(HomeListViewActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_password);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.setCancelable(true);

            EditText edtPassword =  dialog.findViewById(R.id.edtPassword);
            Button btnOk =  dialog.findViewById(R.id.btnOk);
            Button btnCancel =  dialog.findViewById(R.id.btnCancel);

            dialog.show();

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtPassword.getText().toString().equals("6090")){
                        dialog.dismiss();
                        CommonFunctions.getInstance().newIntent(HomeListViewActivity.this, PrintActivity.class, Bundle.EMPTY, false, false);
                    }
                    else {
                        CommonFunctions.getInstance().validationError(HomeListViewActivity.this,"Enter Valid Password");
                    }
                }
            });
        }
    }

    // ----- On BackPressed
    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count == 1) {
            super.onBackPressed();
        }

        if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            CommonFunctions.getInstance().validationEmptyError(HomeListViewActivity.this, "Please click back again to exit.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(HomeListViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(HomeListViewActivity.this, Manifest.permission.CAMERA)
                    + ContextCompat.checkSelfPermission(HomeListViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeListViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(HomeListViewActivity.this, Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(HomeListViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(HomeListViewActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            999);

                } else {
                    ActivityCompat.requestPermissions(HomeListViewActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
                }
            }
        }
    }



}
