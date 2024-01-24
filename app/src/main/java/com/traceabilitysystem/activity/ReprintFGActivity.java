package com.traceabilitysystem.activity;

import static com.traceabilitysystem.utils.CommonFunctions.FG_SSIPL_ID;
import static com.traceabilitysystem.utils.CommonFunctions.Mother_Coil_ID;

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
import com.traceabilitysystem.databinding.ActivityReprintFgBinding;
import com.traceabilitysystem.dummy_model.WIPJson;
import com.traceabilitysystem.model_api.WipApiResponse;
import com.traceabilitysystem.utils.CommonFunctions;

import hari.bounceview.BounceView;

public class ReprintFGActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityReprintFgBinding binding;
    private IntentIntegrator qrScan;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String selectionTab = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reprint_fg);

        qrScan = new IntentIntegrator(this);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        initialView();
    }

    private void initialView() {
        BounceView.addAnimTo(binding.imgBack);

        BounceView.addAnimTo(binding.rlyoutFGSlitter);
        BounceView.addAnimTo(binding.rlyoutFGCutter);

        BounceView.addAnimTo(binding.txtPrintFGSlitter);
        BounceView.addAnimTo(binding.txtPrintFGCutter);

        BounceView.addAnimTo(binding.ImgScanFGSlitter);
        BounceView.addAnimTo(binding.ImgScanFGCutter);

        binding.tvTitle.setText("FG");

        binding.imgBack.setOnClickListener(this);
        binding.rlyoutFGSlitter.setOnClickListener(this);
        binding.rlyoutFGCutter.setOnClickListener(this);
        binding.txtPrintFGSlitter.setOnClickListener(this);
        binding.ImgScanFGSlitter.setOnClickListener(this);
        binding.txtPrintFGCutter.setOnClickListener(this);
        binding.ImgScanFGCutter.setOnClickListener(this);

        // Initial
        selectionTab = "slitter";
        binding.lyoutFGSlitter.setVisibility(View.VISIBLE);
        binding.viewFGSlitter.setVisibility(View.VISIBLE);
        binding.lyoutFGCutter.setVisibility(View.GONE);
        binding.viewFGCutter.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.imgBack){
            finish();
        }
        else if (view == binding.rlyoutFGSlitter){
            selectionTab = "slitter";
            binding.lyoutFGSlitter.setVisibility(View.VISIBLE);
            binding.viewFGSlitter.setVisibility(View.VISIBLE);
            binding.lyoutFGCutter.setVisibility(View.GONE);
            binding.viewFGCutter.setVisibility(View.GONE);
        }
        else if (view == binding.rlyoutFGCutter){
            selectionTab = "cutter";
            binding.lyoutFGSlitter.setVisibility(View.GONE);
            binding.viewFGSlitter.setVisibility(View.GONE);
            binding.lyoutFGCutter.setVisibility(View.VISIBLE);
            binding.viewFGCutter.setVisibility(View.VISIBLE);
        }
        else if (view == binding.ImgScanFGSlitter){
            Bundle bundle = new Bundle();
            bundle.putString("from", "10");
            CommonFunctions.getInstance().newIntent(ReprintFGActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
        }
        else if (view == binding.txtPrintFGSlitter){
            if (binding.edtSSIPLIDFGSlitter.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(ReprintFGActivity.this,"Enter valid SSIPL ID");
            }
            else {
                WIPJson wipJson = new WIPJson();
                wipJson.setMethod("slit_fg");
                wipJson.setOp_batch(binding.edtSSIPLIDFGSlitter.getText().toString().trim());

                Gson gson = new Gson();
                String input = gson.toJson(wipJson);
                System.out.println("Input ==> " + input);

                apiCall(input);
            }
        }
        else if (view == binding.ImgScanFGCutter){
            Bundle bundle = new Bundle();
            bundle.putString("from", "10");
            CommonFunctions.getInstance().newIntent(ReprintFGActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
        }
        else if (view == binding.txtPrintFGCutter){
            if (binding.edtSSIPLIDFGCutter.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(ReprintFGActivity.this,"Enter valid SSIPL ID");
            }
            else {
                WIPJson wipJson = new WIPJson();
                wipJson.setMethod("sheet_fg");
                wipJson.setOp_batch(binding.edtSSIPLIDFGCutter.getText().toString().trim());

                Gson gson = new Gson();
                String input = gson.toJson(wipJson);
                System.out.println("Input ==> " + input);

                apiCall(input);

            }
        }
    }

    private void apiCall(String input) {
        CommonApiCalls.getInstance().wipCall(ReprintFGActivity.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                WipApiResponse apiResponse = (WipApiResponse) body;
                if (apiResponse.getStatus().toLowerCase().equals("success")){
                    CommonFunctions.getInstance().successResponseToast(ReprintFGActivity.this, apiResponse.getMsg());

                    if (selectionTab.equals("slitter")) {
                        binding.edtSSIPLIDFGSlitter.setText("");
                    }
                    else if (selectionTab.equals("cutter")){
                        binding.edtSSIPLIDFGCutter.setText("");
                    }
                }
                else {
                    CommonFunctions.getInstance().validationError(ReprintFGActivity.this, apiResponse.getMsg());
                }
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(ReprintFGActivity.this, reason);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectionTab.equals("slitter")){
            if (!FG_SSIPL_ID.isEmpty()){
                binding.edtSSIPLIDFGSlitter.setText(FG_SSIPL_ID);
                FG_SSIPL_ID = "";
            }
        }
        else if (selectionTab.equals("cutter")){
            if (!FG_SSIPL_ID.isEmpty()){
                binding.edtSSIPLIDFGCutter.setText(FG_SSIPL_ID);
                FG_SSIPL_ID = "";
            }
        }
    }
}
