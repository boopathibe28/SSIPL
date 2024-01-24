package com.traceabilitysystem.activity;

import static com.traceabilitysystem.utils._pref.SharedPrefConstants.LANGUAGE_CODE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.traceabilitysystem.R;
import com.traceabilitysystem.adapter.HelpListAdapter;
import com.traceabilitysystem.api.CommonApiCalls;
import com.traceabilitysystem.app_interfaces.CommonCallback;
import com.traceabilitysystem.databinding.ActivityHelpBinding;
import com.traceabilitysystem.model_api.SettingsApiResponse;
import com.traceabilitysystem.utils.CommonFunctions;
import com.traceabilitysystem.utils.LanguageConstants;
import com.traceabilitysystem.utils._pref.SessionManager;

import java.util.List;

import hari.bounceview.BounceView;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityHelpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);

        if (SessionManager.getInstance().getFromPreference(LANGUAGE_CODE).equals("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            binding.imgBack.setImageResource(R.drawable.ic_color_back_ar);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            binding.imgBack.setImageResource(R.drawable.ic_color_back);
        }

        initialView();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
    }

// ----- Initial View
    private void initialView() {
        BounceView.addAnimTo(binding.imgBack);

        binding.imgBack.setOnClickListener(this);

        binding.tvTitle.setText(LanguageConstants.help);
        binding.txtNoDataFound.setText(LanguageConstants.noDataFound);
    }

// --- OnClick
    @Override
    public void onClick(View view) {
        if (view == binding.imgBack){
            finish();
        }
    }



    private void loadList(List<SettingsApiResponse.Datum> data) {
        binding.swipeRefreshLayout.setRefreshing(false);
        if (data.size() > 0){
            binding.rvHelp.setVisibility(View.VISIBLE);
            binding.txtNoDataFound.setVisibility(View.GONE);

            @SuppressLint("WrongConstant")
            LinearLayoutManager layoutManager = new LinearLayoutManager(HelpActivity.this, LinearLayoutManager.VERTICAL, false);
            binding.rvHelp.setLayoutManager(layoutManager);
            HelpListAdapter adapter = new HelpListAdapter(HelpActivity.this, data);
            binding.rvHelp.setAdapter(adapter);
        }
        else {
            binding.rvHelp.setVisibility(View.GONE);
            binding.txtNoDataFound.setVisibility(View.VISIBLE);
        }
    }

}
