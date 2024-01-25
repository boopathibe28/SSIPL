package com.traceabilitysystem.activity;


import static com.traceabilitysystem.utils.CommonFunctions.OP_BATCH;
import static com.traceabilitysystem.utils.CommonFunctions.locationID;
import static com.traceabilitysystem.utils.CommonFunctions.ssiplID;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.traceabilitysystem.R;
import com.traceabilitysystem.api.CommonApiCalls;
import com.traceabilitysystem.app_interfaces.CommonCallback;
import com.traceabilitysystem.databinding.ActivityOpBatchCorrectionBinding;
import com.traceabilitysystem.dummy_model.OPBatchDetailsApiResponse;
import com.traceabilitysystem.dummy_model.OPBatchJSON;
import com.traceabilitysystem.dummy_model.OPBatchPrintApiResponse;
import com.traceabilitysystem.dummy_model.OPBatchPrintJSON;
import com.traceabilitysystem.utils.CommonFunctions;

import java.text.DecimalFormat;
import java.util.Locale;

import hari.bounceview.BounceView;

public class OPBatchCorrection extends AppCompatActivity implements View.OnClickListener {
    ActivityOpBatchCorrectionBinding binding;
    private IntentIntegrator qrScan;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private float ansWeight;
    private float ansLength;
    private float ansWidth;
    private float ansThick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_op_batch_correction);

        qrScan = new IntentIntegrator(OPBatchCorrection.this);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        initialView();

    }
    private void initialView() {
        BounceView.addAnimTo(binding.imgBack);
        BounceView.addAnimTo(binding.imgScanner);
        BounceView.addAnimTo(binding.txtGo);
        BounceView.addAnimTo(binding.txtPrint);

        binding.imgBack.setOnClickListener(this);
        binding.imgScanner.setOnClickListener(this);
        binding.txtGo.setOnClickListener(this);

        binding.lyoutType.setOnClickListener(this);
        binding.edtType.setOnClickListener(this);
        binding.imgeType.setOnClickListener(this);
        binding.txtPrint.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.imgBack){
            finish();
        }
        else if (view == binding.imgScanner){
            Bundle bundle = new Bundle();
            bundle.putString("from", "13");
            CommonFunctions.getInstance().newIntent(OPBatchCorrection.this, ScannedBarcodeActivity.class, bundle, false, false);
        }
        else if (view == binding.txtGo){
            if (binding.edtOpBatch.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(OPBatchCorrection.this,"Enter valid OP Batch Number");
            }
            else {
                OPBatchJSON opBatchJSON = new OPBatchJSON();
                opBatchJSON.setMethod("get_opbatch_detail");
                opBatchJSON.setOp_batch(binding.edtOpBatch.getText().toString().trim());

                Gson gson = new Gson();
                String input = gson.toJson(opBatchJSON);
                System.out.println("Input ==> " + input);

                OpBatchDetailsApiCall(input);
            }
        }
        else if (view == binding.lyoutType || view == binding.edtType || view == binding.imgeType){
            typeDialog();
        }
        else if (view == binding.txtPrint){
            if (binding.edtCustomerCode.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(OPBatchCorrection.this,"Enter valid Customer Code");
            }
            else if (binding.edtCustomerName.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(OPBatchCorrection.this,"Enter valid Customer Name");
            }
            else if (binding.edtThick.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(OPBatchCorrection.this,"Enter valid Thick");
            }
            else if (binding.edtWidth.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(OPBatchCorrection.this,"Enter valid Width");
            }
            else if (binding.edtLength.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(OPBatchCorrection.this,"Enter valid Length");
            }
            else if (binding.edtGrossWeight.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(OPBatchCorrection.this,"Enter valid Gross Weight");
            }
            else {
                DecimalFormat decimalFormat = new DecimalFormat("#.###");

                String thick = binding.edtThick.getText().toString().trim().replace(",", ".");
                Double thick_value = Double.valueOf(thick);
                ansThick = Float.valueOf(decimalFormat.format(thick_value));

                String width = binding.edtWidth.getText().toString().trim().replace(",", ".");
                Double width_value = Double.valueOf(width);
                ansWidth = Float.valueOf(decimalFormat.format(width_value));

                String length = binding.edtLength.getText().toString().trim().replace(",", ".");
                Double length_value = Double.valueOf(length);
                ansLength = Float.valueOf(decimalFormat.format(length_value));

                String weight = binding.edtGrossWeight.getText().toString().trim().replace(",", ".");
                Double weight_value = Double.valueOf(weight);
                ansWeight = Float.valueOf(decimalFormat.format(weight_value));

                OPBatchPrintJSON opBatchPrintJSON = new OPBatchPrintJSON();
                opBatchPrintJSON.setMethod(binding.edtType.getText().toString().trim());
                opBatchPrintJSON.setEdit("true");
                opBatchPrintJSON.setCustomer_name(binding.edtCustomerName.getText().toString().trim());
                opBatchPrintJSON.setCustomer_code(binding.edtCustomerCode.getText().toString().trim());
                opBatchPrintJSON.setOp_batch(binding.edtOpBatch.getText().toString().trim());

               /* opBatchPrintJSON.setGross_weight(binding.edtGrossWeight.getText().toString().trim());
                opBatchPrintJSON.setThick(binding.edtThick.getText().toString().trim());
                opBatchPrintJSON.setWidth(binding.edtWidth.getText().toString().trim());
                opBatchPrintJSON.setLength(binding.edtLength.getText().toString().trim());*/

                opBatchPrintJSON.setGross_weight(ansWeight);
                opBatchPrintJSON.setThick(ansThick);
                opBatchPrintJSON.setWidth(ansWidth);
                opBatchPrintJSON.setLength(ansLength);

                Gson gson = new Gson();
                String input = gson.toJson(opBatchPrintJSON);
                System.out.println("Input ==> " + input);

                OpBatchPrintApiCall(input);
            }
        }
    }

    private void typeDialog() {
        final Dialog dialog = new Dialog(OPBatchCorrection.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_type_op_batch);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);

        ImageView imgClose =  dialog.findViewById(R.id.imgClose);
        TextView txtHeader =  dialog.findViewById(R.id.txtHeader);
        TextView txtSlitFg =  dialog.findViewById(R.id.txtSlitFg);
        TextView txtSheetFg =  dialog.findViewById(R.id.txtSheetFg);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtSlitFg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edtType.setText("slit_fg");
                dialog.dismiss();
            }
        });

        txtSheetFg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edtType.setText("sheet_fg");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void OpBatchDetailsApiCall(String input) {
        CommonApiCalls.getInstance().opBatchDetails(OPBatchCorrection.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                OPBatchDetailsApiResponse apiResponse = (OPBatchDetailsApiResponse) body;
                if (apiResponse.getStatus().toLowerCase().equals("success")){
                    CommonFunctions.getInstance().successResponseToast(OPBatchCorrection.this, apiResponse.getMsg());

                    binding.lyoutOpBatchDetails.setVisibility(View.VISIBLE);

                    binding.edtCustomerCode.setText(apiResponse.getResult().getCustomer_Code());
                    binding.edtCustomerName.setText(apiResponse.getResult().getCustomer_Name());

                    if (apiResponse.getResult().getThick() != null && !apiResponse.getResult().getThick().isEmpty()) {
                       if (apiResponse.getResult().getThick().contains(".")){
                           String thick = apiResponse.getResult().getThick().replace(",", ".");

                           DecimalFormat decimalFormat = new DecimalFormat("#.###");
                           Double thick_value = Double.valueOf(thick);
                           ansThick = Float.valueOf(decimalFormat.format(thick_value));
                           binding.edtThick.setText(thick_value+"");
                       }
                       else {
                           binding.edtThick.setText(apiResponse.getResult().getThick());
                       }
                    }


                    if (apiResponse.getResult().getWidth() != null && !apiResponse.getResult().getWidth().isEmpty()) {
                        if (apiResponse.getResult().getWidth().contains(".")){
                            String width = apiResponse.getResult().getWidth().replace(",", ".");

                            DecimalFormat decimalFormat = new DecimalFormat("#.###");
                            Double width_value = Double.valueOf(width);
                            ansWidth = Float.valueOf(decimalFormat.format(width_value));
                            binding.edtWidth.setText(ansWidth+"");
                        }
                        else {
                            binding.edtWidth.setText(apiResponse.getResult().getWidth());
                        }
                    }

                    if (apiResponse.getResult().getLength() != null && !apiResponse.getResult().getLength().isEmpty()) {
                        if (apiResponse.getResult().getLength().contains(".")){
                            String length = apiResponse.getResult().getLength().replace(",", ".");

                            DecimalFormat decimalFormat = new DecimalFormat("#.###");
                            Double length_value = Double.valueOf(length);
                            ansLength = Float.valueOf(decimalFormat.format(length_value));
                            binding.edtLength.setText(ansLength+"");
                        }
                        else {
                            binding.edtLength.setText(apiResponse.getResult().getLength());
                        }
                    }

                    if (apiResponse.getResult().getGross_Weight() != null && !apiResponse.getResult().getGross_Weight().isEmpty()) {
                        if (apiResponse.getResult().getGross_Weight().contains(".")){
                            String weight = apiResponse.getResult().getGross_Weight().replace(",", ".");

                            DecimalFormat decimalFormat = new DecimalFormat("#.###");
                            Double weight_value = Double.valueOf(weight);
                            ansWeight = Float.valueOf(decimalFormat.format(weight_value));
                            binding.edtGrossWeight.setText(ansWeight+"");
                        }
                        else {
                            binding.edtGrossWeight.setText(apiResponse.getResult().getGross_Weight());
                        }
                    }

                   /* binding.edtThick.setText(apiResponse.getResult().getThick());
                    binding.edtWidth.setText(apiResponse.getResult().getWidth());
                    binding.edtLength.setText(apiResponse.getResult().getLength());
                    binding.edtGrossWeight.setText(apiResponse.getResult().getGross_Weight());*/
                }
                else {
                    CommonFunctions.getInstance().validationError(OPBatchCorrection.this, apiResponse.getMsg());
                }
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(OPBatchCorrection.this, reason);
            }
        });

    }


    private void OpBatchPrintApiCall(String input) {
        CommonApiCalls.getInstance().opBatchPrint(OPBatchCorrection.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                OPBatchPrintApiResponse apiResponse = (OPBatchPrintApiResponse) body;
                if (apiResponse.getStatus().toLowerCase().equals("success")){
                    CommonFunctions.getInstance().successResponseToast(OPBatchCorrection.this, apiResponse.getMsg());

                    binding.lyoutOpBatchDetails.setVisibility(View.GONE);

                    binding.edtCustomerCode.setText("");
                    binding.edtCustomerName.setText("");
                    binding.edtThick.setText("");
                    binding.edtWidth.setText("");
                    binding.edtLength.setText("");
                    binding.edtGrossWeight.setText("");
                    binding.edtType.setText("");
                }
                else {
                    CommonFunctions.getInstance().validationError(OPBatchCorrection.this, apiResponse.getMsg());
                }
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(OPBatchCorrection.this, reason);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!OP_BATCH.isEmpty()){
            binding.edtOpBatch.setText(OP_BATCH);

            OP_BATCH = "";
        }
    }
}
