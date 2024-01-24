package com.traceabilitysystem.activity;

import static com.traceabilitysystem.utils.CommonFunctions.BAR_CODE_VALUE;
import static com.traceabilitysystem.utils.CommonFunctions.ansCoilNo;
import static com.traceabilitysystem.utils.CommonFunctions.ansSize;
import static com.traceabilitysystem.utils.CommonFunctions.ansWeight;
import static com.traceabilitysystem.utils.CommonFunctions.ssiplID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.traceabilitysystem.R;
import com.traceabilitysystem.api.CommonApiCalls;
import com.traceabilitysystem.app_interfaces.CommonCallback;
import com.traceabilitysystem.databinding.ActivityRminventoryBinding;
import com.traceabilitysystem.dummy_model.GrnProcessJson;
import com.traceabilitysystem.dummy_model.RMInventoryJson;
import com.traceabilitysystem.dummy_model.SSIPLidJson;
import com.traceabilitysystem.model_api.GrnProcessCheckApiResponse;
import com.traceabilitysystem.model_api.GrnProcessPrintBarCodeApiResponse;
import com.traceabilitysystem.model_api.RMInventoryApiResponse;
import com.traceabilitysystem.model_api.SsiplIDApiResponse;
import com.traceabilitysystem.utils.CommonFunctions;

import hari.bounceview.BounceView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RMInventoryActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRminventoryBinding binding;
    private IntentIntegrator qrScan;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String selectedTab = "";
    private String GrnData = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rminventory);

        qrScan = new IntentIntegrator(RMInventoryActivity.this);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        initialView();
    }

    private void initialView() {
        BounceView.addAnimTo(binding.imgBack);
        BounceView.addAnimTo(binding.imgScanner);
        BounceView.addAnimTo(binding.txtDelete);
        BounceView.addAnimTo(binding.txtAccept);
        BounceView.addAnimTo(binding.txtNextPage);

        BounceView.addAnimTo(binding.rlyoutInward);
        BounceView.addAnimTo(binding.rlyoutSSIPLID);
        BounceView.addAnimTo(binding.rlyoutGRNProcess);

        binding.imgBack.setOnClickListener(this);
        binding.imgScanner.setOnClickListener(this);
        binding.txtNextPage.setOnClickListener(this);
        binding.txtDelete.setOnClickListener(this);
        binding.txtAccept.setOnClickListener(this);

        binding.rlyoutInward.setOnClickListener(this);
        binding.rlyoutSSIPLID.setOnClickListener(this);
        binding.rlyoutGRNProcess.setOnClickListener(this);

        binding.tvTitle.setText("RM Inventory");

        binding.viewInward.setVisibility(View.VISIBLE);
        binding.viewSSIPLID.setVisibility(View.GONE);
        binding.viewGRNProcess.setVisibility(View.GONE);

        binding.lyoutInward.setVisibility(View.VISIBLE);
        binding.lyoutSSIPLID.setVisibility(View.GONE);
        binding.lyoutGRNProcess.setVisibility(View.GONE);
        selectedTab = "inward";


        // SSipl
        BounceView.addAnimTo(binding.txtHomeSSIPL);
        BounceView.addAnimTo(binding.txtNotOkSSIPL);
        BounceView.addAnimTo(binding.txtOkSSIPL);
        binding.txtNotOkSSIPL.setOnClickListener(this);
        binding.txtOkSSIPL.setOnClickListener(this);
        binding.txtHomeSSIPL.setOnClickListener(this);

        // GRN
        BounceView.addAnimTo(binding.txtPrintGRN);
        BounceView.addAnimTo(binding.txtHomeGRN);
        binding.txtPrintGRN.setOnClickListener(this);
        binding.txtHomeGRN.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == binding.imgBack){
            finish();
        }
        else if (view == binding.rlyoutInward){
            selectedTab = "inward";
            binding.viewInward.setVisibility(View.VISIBLE);
            binding.viewSSIPLID.setVisibility(View.GONE);
            binding.viewGRNProcess.setVisibility(View.GONE);

            binding.lyoutInward.setVisibility(View.VISIBLE);
            binding.lyoutSSIPLID.setVisibility(View.GONE);
            binding.lyoutGRNProcess.setVisibility(View.GONE);
        }
        else if (view == binding.rlyoutSSIPLID){
            selectedTab = "ssipl";
            binding.viewInward.setVisibility(View.GONE);
            binding.viewSSIPLID.setVisibility(View.VISIBLE);
            binding.viewGRNProcess.setVisibility(View.GONE);

            binding.lyoutInward.setVisibility(View.GONE);
            binding.lyoutSSIPLID.setVisibility(View.VISIBLE);
            binding.lyoutGRNProcess.setVisibility(View.GONE);
        }
        else if (view == binding.rlyoutGRNProcess){
            selectedTab = "grn";
            binding.viewInward.setVisibility(View.GONE);
            binding.viewSSIPLID.setVisibility(View.GONE);
            binding.viewGRNProcess.setVisibility(View.VISIBLE);

            binding.lyoutInward.setVisibility(View.GONE);
            binding.lyoutSSIPLID.setVisibility(View.GONE);
            binding.lyoutGRNProcess.setVisibility(View.VISIBLE);
            buttonPrint();
        }
        else if (view == binding.imgScanner){
            if (selectedTab.equals("inward")) {
                Bundle bundle = new Bundle();
                bundle.putString("from", "1");
                CommonFunctions.getInstance().newIntent(RMInventoryActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
            }
            else if (selectedTab.equals("ssipl")){
                if (!binding.edtCoilNoSSIPL.getText().toString().trim().isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "4");
                    CommonFunctions.getInstance().newIntent(RMInventoryActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "1");
                    CommonFunctions.getInstance().newIntent(RMInventoryActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
                }
            }
            else if (selectedTab.equals("grn")){
                Bundle bundle = new Bundle();
                bundle.putString("from","4");
                CommonFunctions.getInstance().newIntent(RMInventoryActivity.this, ScannedBarcodeActivity.class, bundle, false, false);
            }
        }
        else if (view == binding.txtDelete){
            binding.edtCoilNo.setText("");
            binding.edtBarcode.setText("");
            binding.edtSize.setText("");
            binding.edtGrade.setText("");
            binding.edtWeight.setText("");
            binding.edtHeatNo.setText("");
        }
        else if (view == binding.txtNextPage){
            CommonFunctions.getInstance().newIntent(RMInventoryActivity.this, SSIPLIDProcessActivity.class, Bundle.EMPTY, false, false);
        }
        else if (view == binding.txtAccept){
            if (binding.edtCoilNo.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(RMInventoryActivity.this,"Mother Coil ID is Empty");
            }
            /*else if (binding.edtSize.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(RMInventoryActivity.this,"Size is Empty");
            }
            else if (binding.edtWeight.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(RMInventoryActivity.this,"Weight is Empty");
            }*/
            else {
                RMInventoryJson rmInventoryJson = new RMInventoryJson();
                rmInventoryJson.setCoil_No(binding.edtCoilNo.getText().toString().trim());
                rmInventoryJson.setSize(binding.edtSize.getText().toString().trim());
                rmInventoryJson.setNet_Weight(binding.edtWeight.getText().toString().trim());
                rmInventoryJson.setType("RM Inv");

                Gson gson = new Gson();
                String input = gson.toJson(rmInventoryJson);
                System.out.println("Input ==> " + input);

                apiCall(input);
            }
        }
        else if (view == binding.txtOkSSIPL){

        }
        else if (view == binding.txtNotOkSSIPL){
            binding.edtCoilNoSSIPL.setText("");
            binding.edtBarcodeSSIPL.setText("");
            binding.edtSizeSSIPL.setText("");
            binding.edtGradeSSIPL.setText("");
            binding.edtWeightSSIPL.setText("");
            binding.edtHeatNoSSIPL.setText("");
            binding.edtSSIPLIDSSIPL.setText("");
        }
        else if (view == binding.txtHomeSSIPL){
            CommonFunctions.getInstance().newIntent(RMInventoryActivity.this, HomeListViewActivity.class, Bundle.EMPTY, true, true);
        }
        else if (view == binding.txtPrintGRN){
            if (binding.edtSSIPLIDGRN.getText().toString().trim().isEmpty()){
                CommonFunctions.getInstance().validationError(RMInventoryActivity.this,"SSIPL ID is Empty");
            }
            else {
                if (GrnData.equals("1")){
                    GrnProcessJson grnProcessJson = new GrnProcessJson();
                    grnProcessJson.setMethod("print");
                    grnProcessJson.setSsipl(binding.edtSSIPLIDGRN.getText().toString().trim());

                    Gson gson = new Gson();
                    String input = gson.toJson(grnProcessJson);
                    System.out.println("Input ==> " + input);

                    CommonApiCalls.getInstance().GrnProcessPrintBarCode(RMInventoryActivity.this, input, new CommonCallback.Listener() {
                        @Override
                        public void onSuccess(Object body) {
                            GrnProcessPrintBarCodeApiResponse apiResponse = (GrnProcessPrintBarCodeApiResponse) body;

                            Toast.makeText(RMInventoryActivity.this,apiResponse.getMsg(),Toast.LENGTH_LONG);
                            // CommonFunctions.getInstance().successResponseToast(GRNProcess.this, apiResponse.getMsg());
                        }

                        @Override
                        public void onFailure(String reason) {
                            CommonFunctions.getInstance().validationEmptyError(RMInventoryActivity.this, reason);
                        }
                    });
                }
            }
        }
        else if (view == binding.txtHomeGRN){
            finish();
        }

    }

    private void apiCall(String input) {
        CommonApiCalls.getInstance().rmInventory(RMInventoryActivity.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                RMInventoryApiResponse apiResponse = (RMInventoryApiResponse) body;

                if (apiResponse.getStatus().toLowerCase().equals("failure")){
                    CommonFunctions.getInstance().validationError(RMInventoryActivity.this, apiResponse.getMsg());
                }
                else {
                    CommonFunctions.getInstance().successResponseToast(RMInventoryActivity.this, apiResponse.getMsg());
                }
                binding.edtCoilNo.setText("");
                binding.edtBarcode.setText("");
                binding.edtSize.setText("");
                binding.edtWeight.setText("");
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(RMInventoryActivity.this, reason);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RMInventoryActivity.RESULT_OK) {
            if (data == null)
                return;
            //Getting the passed result
            String result1 = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result1 != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(RMInventoryActivity.this).create();
                alertDialog.setTitle("Scan error");
                alertDialog.setMessage("QR code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result2 = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            String result[] = result2.split("key=");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedTab.equals("inward")) {
            if (!ansCoilNo.isEmpty()) {
                binding.edtCoilNo.setText(ansCoilNo);
                binding.edtBarcode.setText(BAR_CODE_VALUE);
                binding.edtSize.setText(ansSize);
                binding.edtWeight.setText(ansWeight);

                ansCoilNo = "";
                ansSize = "";
                ansWeight = "";
                BAR_CODE_VALUE = "";
            }
        }
        else if (selectedTab.equals("ssipl")){
            if (!ansCoilNo.isEmpty()){
                binding.edtCoilNoSSIPL.setText(ansCoilNo);
                binding.edtSizeSSIPL.setText(ansSize);
                binding.edtWeightSSIPL.setText(ansWeight);

                ansCoilNo = "";
                ansSize = "";
                ansWeight = "";
            }
            if (!ssiplID.isEmpty()){
                binding.edtSSIPLIDSSIPL.setText(ssiplID);

                ssiplID = "";
            }

            // api call
            if (!binding.edtCoilNoSSIPL.getText().toString().trim().isEmpty() &&
                    !binding.edtSSIPLIDSSIPL.getText().toString().trim().isEmpty() ){
                setValue();
            }
        }
        else if (selectedTab.equals("grn")){
            if (!ssiplID.isEmpty()){
                binding.edtSSIPLIDGRN.setText(ssiplID);
                ssiplID = "";

                checkGrnProcess();
            }
        }
    }

// SSIPL ID
    private void setValue() {
        SSIPLidJson ssipLidJson = new SSIPLidJson();
        ssipLidJson.setSSIPL_Id(binding.edtSSIPLIDSSIPL.getText().toString().trim());
        ssipLidJson.setCoil_No(binding.edtCoilNoSSIPL.getText().toString().trim());

        Gson gson = new Gson();
        String input = gson.toJson(ssipLidJson);
        System.out.println("Input ==> " + input);

        apiCallSSIPL(input);
    }

    private void apiCallSSIPL(String input) {
        CommonApiCalls.getInstance().ssiplID(RMInventoryActivity.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                SsiplIDApiResponse apiResponse = (SsiplIDApiResponse) body;

                if (apiResponse.getResult().equals("1")){
                    binding.txtOkSSIPL.setVisibility(View.VISIBLE);
                    binding.txtNotOkSSIPL.setVisibility(View.GONE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CommonFunctions.getInstance().successResponseToast(RMInventoryActivity.this, apiResponse.getStatus());

                            binding.edtCoilNoSSIPL.setText("");
                            binding.edtBarcodeSSIPL.setText("");
                            binding.edtSizeSSIPL.setText("");
                            binding.edtGradeSSIPL.setText("");
                            binding.edtWeightSSIPL.setText("");
                            binding.edtHeatNoSSIPL.setText("");
                            binding.edtSSIPLIDSSIPL.setText("");
                        }
                    }, 900);

                }
                else {
                    binding.txtOkSSIPL.setVisibility(View.GONE);
                    binding.txtNotOkSSIPL.setVisibility(View.VISIBLE);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CommonFunctions.getInstance().newIntent(RMInventoryActivity.this, HomeListViewActivity.class, Bundle.EMPTY, true, true);
                    }
                }, 4500);
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(RMInventoryActivity.this, reason);
            }
        });
    }


    // --- GRN
    private void checkGrnProcess() {
        GrnProcessJson grnProcessJson = new GrnProcessJson();
        grnProcessJson.setMethod("check");
        grnProcessJson.setSsipl(binding.edtSSIPLIDGRN.getText().toString().trim());

        Gson gson = new Gson();
        String input = gson.toJson(grnProcessJson);
        System.out.println("Input ==> " + input);

        apiCallGrn(input);
    }


    private void apiCallGrn(String input) {
        CommonApiCalls.getInstance().GrnProcessCheck(RMInventoryActivity.this, input, new CommonCallback.Listener() {
            @Override
            public void onSuccess(Object body) {
                GrnProcessCheckApiResponse apiResponse = (GrnProcessCheckApiResponse) body;
                GrnData = apiResponse.getResult();
                buttonPrint();
                if (GrnData.equals("1")) {
                    CommonFunctions.getInstance().successResponseToast(RMInventoryActivity.this, apiResponse.getMsg());
                }
                else {
                    //CommonFunctions.getInstance().validationError(GRNProcess.this, apiResponse.getMsg());
                    //  CommonFunctions.getInstance().validationError_Custom(GRNProcess.this,apiResponse.getMsg());

                    binding.txtErrorToastGRN.setVisibility(View.VISIBLE);
                    binding.txtErrorToastGRN.setText(apiResponse.getMsg());
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    binding.txtErrorToastGRN.startAnimation(anim);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.txtErrorToastGRN.setVisibility(View.GONE);
                        }
                    }, 3000);
                }
            }

            @Override
            public void onFailure(String reason) {
                CommonFunctions.getInstance().validationEmptyError(RMInventoryActivity.this, reason);
            }
        });

    }


    private void buttonPrint() {
        if (GrnData.equals("1")){
            binding.txtPrintGRN.setVisibility(View.VISIBLE);
            binding.txtPrintGRN.setTextColor(ContextCompat.getColor(RMInventoryActivity.this, R.color.black));
            binding.txtPrintGRN.setBackground(ContextCompat.getDrawable(RMInventoryActivity.this, R.drawable.bg_rescan));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.txtPrintGRN.setVisibility(View.GONE);
                }
            }, 60000);
        }
        else {
            binding.txtPrintGRN.setVisibility(View.GONE);
            binding.txtPrintGRN.setTextColor(ContextCompat.getColor(RMInventoryActivity.this, R.color.txt_gray));

            binding.txtPrintGRN.setBackground(ContextCompat.getDrawable(RMInventoryActivity.this, R.drawable.bg_print_diasble));
        }
    }
}
