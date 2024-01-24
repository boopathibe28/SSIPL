package com.traceabilitysystem.activity;

import static com.traceabilitysystem.utils.CommonFunctions.locationID;
import static com.traceabilitysystem.utils.CommonFunctions.ssiplID;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.traceabilitysystem.R;
import com.traceabilitysystem.api.CommonApiCalls;
import com.traceabilitysystem.app_interfaces.CommonCallback;
import com.traceabilitysystem.databinding.ActivityWipStoreBinding;
import com.traceabilitysystem.dummy_model.FgGridStorageJson;
import com.traceabilitysystem.dummy_model.WIPStoreJson;
import com.traceabilitysystem.model_api.RMStorageApiResponse;
import com.traceabilitysystem.model_api.WIPStorageApiResponse;
import com.traceabilitysystem.utils.CommonFunctions;

import hari.bounceview.BounceView;

public class WIPStoreActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityWipStoreBinding binding;
    private IntentIntegrator qrScan;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_wip_store);

        qrScan = new IntentIntegrator(this);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        initialView();
    }

    private void initialView() {
        BounceView.addAnimTo(binding.imgBack);
        BounceView.addAnimTo(binding.txtDelete);
        BounceView.addAnimTo(binding.txtAccept);

        binding.imgBack.setOnClickListener(this);
        binding.txtDelete.setOnClickListener(this);
        binding.txtAccept.setOnClickListener(this);

        binding.lyoutSSIPLID.setOnClickListener(this);
        binding.edtSSIPLID.setOnClickListener(this);

        binding.lyoutLocationID.setOnClickListener(this);
        binding.edtLocationID.setOnClickListener(this);

        binding.tvTitle.setText("WIP Storage");
    }

    @Override
    public void onClick(View view) {
        if (view == binding.imgBack){
            finish();
        }
        else if (view == binding.edtSSIPLID || view == binding.lyoutSSIPLID){
            Bundle bundle = new Bundle();
            bundle.putString("from","11");
            CommonFunctions.getInstance().newIntent(WIPStoreActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
        }
        else if (view == binding.edtLocationID || view == binding.lyoutLocationID){
            Bundle bundle = new Bundle();
            bundle.putString("from","5");
            CommonFunctions.getInstance().newIntent(WIPStoreActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
        }
        else if (view == binding.txtDelete){
            binding.edtSSIPLID.setText("");
            binding.edtBarcode.setText("");
            binding.edtLocationID.setText("");
            binding.edtGridNo.setText("");
            binding.edtGridSubNo.setText("");
            binding.edtRemarks.setText("");
        }
        else if (view == binding.txtAccept){
            if (binding.edtSSIPLID.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(WIPStoreActivity.this,"SSIPL ID is Empty");
            }
            else if (binding.edtLocationID.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(WIPStoreActivity.this,"Location ID is Empty");
            }
            else if (binding.edtGridNo.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(WIPStoreActivity.this,"Grid No is Empty");
            }
            else if (binding.edtGridSubNo.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(WIPStoreActivity.this,"Grid Sub No is Empty");
            }
            else {
                WIPStoreJson wipStoreJson = new WIPStoreJson();
                wipStoreJson.setSSIPL_Id(binding.edtSSIPLID.getText().toString().trim());
                wipStoreJson.setLocation_Id(binding.edtLocationID.getText().toString().trim());
                wipStoreJson.setGrid_No(binding.edtGridNo.getText().toString().trim());
                wipStoreJson.setGrid_Sub_No(binding.edtGridSubNo.getText().toString().trim());
                wipStoreJson.setType("WIP Store");

                Gson gson = new Gson();
                String input = gson.toJson(wipStoreJson);
                System.out.println("Input ==> " + input);

                apiCall(input);
            }

        }
    }


    private void apiCall(String input) {
        CommonApiCalls.getInstance().wipStore(WIPStoreActivity.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                WIPStorageApiResponse apiResponse = (WIPStorageApiResponse) body;
                CommonFunctions.getInstance().successResponseToast(WIPStoreActivity.this, apiResponse.getMsg());

                binding.edtSSIPLID.setText("");
                binding.edtBarcode.setText("");
                binding.edtLocationID.setText("");
                binding.edtGridNo.setText("");
                binding.edtGridSubNo.setText("");
                binding.edtRemarks.setText("");
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(WIPStoreActivity.this, reason);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ssiplID.isEmpty()){
            binding.edtSSIPLID.setText(ssiplID);
            binding.edtBarcode.setText(ssiplID);

            ssiplID = "";
        }

        if (!locationID.isEmpty()){
            binding.edtLocationID.setText(locationID);
            binding.edtBarcode.setText(locationID);

            locationID = "";
        }
    }

}
