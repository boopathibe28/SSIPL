package com.traceabilitysystem.activity;

import static com.traceabilitysystem.utils.CommonFunctions.ssiplID;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.traceabilitysystem.R;
import com.traceabilitysystem.api.CommonApiCalls;
import com.traceabilitysystem.app_interfaces.CommonCallback;
import com.traceabilitysystem.databinding.ActivitySalesReturnBinding;
import com.traceabilitysystem.dummy_model.GrnProcessJson;
import com.traceabilitysystem.dummy_model.SalesReturnJson;
import com.traceabilitysystem.model_api.GrnProcessCheckApiResponse;
import com.traceabilitysystem.model_api.GrnProcessPrintBarCodeApiResponse;
import com.traceabilitysystem.model_api.SalesReturnCheckApiResponse;
import com.traceabilitysystem.model_api.SalesReturnPrintBarCodeApiResponse;
import com.traceabilitysystem.utils.CommonFunctions;

import hari.bounceview.BounceView;

public class SalesReturn extends AppCompatActivity implements View.OnClickListener {
    ActivitySalesReturnBinding binding;
    private IntentIntegrator qrScan;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private String returnResponce = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sales_return);

        qrScan = new IntentIntegrator(SalesReturn.this);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        initialView();
    }


    private void initialView() {
        BounceView.addAnimTo(binding.imgBack);
        BounceView.addAnimTo(binding.imgScanner);
        BounceView.addAnimTo(binding.txtPrint);
        BounceView.addAnimTo(binding.txtEnter);

        binding.imgBack.setOnClickListener(this);
        binding.imgScanner.setOnClickListener(this);
        binding.txtPrint.setOnClickListener(this);
        binding.txtEnter.setOnClickListener(this);

        binding.tvTitle.setText("Sales Return");
    }

    @Override
    public void onClick(View view) {
        if (view == binding.imgBack){
            finish();
        }
        else if (view == binding.imgScanner){
            Bundle bundle = new Bundle();
            bundle.putString("from","6");
            CommonFunctions.getInstance().newIntent(SalesReturn.this, ScannedBarcodeActivity.class, bundle, false, false);
        }
        else if (view == binding.txtEnter){
            if (binding.edtSSIPLID.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(SalesReturn.this,"SSIPL ID is Empty");
            }
            else {
                checkSalesReturn();
            }
        }
        else if (view == binding.txtPrint){
            if (binding.edtSSIPLID.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(SalesReturn.this,"SSIPL ID is Empty");
            }
            else {
                if (returnResponce.equals("1")){
                    GrnProcessJson grnProcessJson = new GrnProcessJson();
                    grnProcessJson.setMethod("print");
                    grnProcessJson.setSsipl(binding.edtSSIPLID.getText().toString().trim());

                    Gson gson = new Gson();
                    String input = gson.toJson(grnProcessJson);
                    System.out.println("Input ==> " + input);

                    CommonApiCalls.getInstance().salesReturnPrintBarCode(SalesReturn.this, input, new CommonCallback.Listener() {
                        @Override
                        public void onSuccess(Object body) {
                            SalesReturnPrintBarCodeApiResponse apiResponse = (SalesReturnPrintBarCodeApiResponse) body;

                            Toast.makeText(SalesReturn.this,apiResponse.getMsg(),Toast.LENGTH_LONG);
                            // CommonFunctions.getInstance().successResponseToast(GRNProcess.this, apiResponse.getMsg());
                        }

                        @Override
                        public void onFailure(String reason) {
                            CommonFunctions.getInstance().validationEmptyError(SalesReturn.this, reason);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ssiplID.isEmpty()){
            binding.edtSSIPLID.setText(ssiplID);
            ssiplID = "";

            checkSalesReturn();
        }
    }

    private void checkSalesReturn() {
        SalesReturnJson salesReturnJson = new SalesReturnJson();
        salesReturnJson.setMethod("check");
        salesReturnJson.setSsipl(binding.edtSSIPLID.getText().toString().trim());

        Gson gson = new Gson();
        String input = gson.toJson(salesReturnJson);
        System.out.println("Input ==> " + input);

        apiCall(input);
    }


    private void apiCall(String input) {
        CommonApiCalls.getInstance().salesReturnCheck(SalesReturn.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                SalesReturnCheckApiResponse apiResponse = (SalesReturnCheckApiResponse) body;
                returnResponce = apiResponse.getResult();
                if (returnResponce.equals("1")) {
                    buttonPrint();
                    CommonFunctions.getInstance().successResponseToast(SalesReturn.this, apiResponse.getMsg());
                }
                else {
                    //CommonFunctions.getInstance().validationError(GRNProcess.this, apiResponse.getMsg());
                   // CommonFunctions.getInstance().validationError_Custom(SalesReturn.this,apiResponse.getMsg());

                    binding.txtErrorToast.setVisibility(View.VISIBLE);
                    binding.txtErrorToast.setText(apiResponse.getMsg());
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    binding.txtErrorToast.startAnimation(anim);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.txtErrorToast.setVisibility(View.GONE);
                        }
                    }, 3000);
                }
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(SalesReturn.this, reason);
            }
        });

    }


    private void buttonPrint() {
        if (returnResponce.equals("1")){
            binding.txtPrint.setVisibility(View.VISIBLE);
            binding.txtPrint.setTextColor(ContextCompat.getColor(SalesReturn.this, R.color.black));
            binding.txtPrint.setBackground(ContextCompat.getDrawable(SalesReturn.this, R.drawable.bg_rescan));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.txtPrint.setVisibility(View.GONE);
                }
            }, 60000);
        }
        else {
            binding.txtPrint.setVisibility(View.GONE);
        }
    }


}
