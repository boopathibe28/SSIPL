package com.traceabilitysystem.activity;

import static com.traceabilitysystem.utils._pref.SharedPrefConstants.LANGUAGE_CODE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.traceabilitysystem.R;
import com.traceabilitysystem.adapter.HelpListAdapter;
import com.traceabilitysystem.databinding.AdapterHelpListBinding;
import com.traceabilitysystem.databinding.AdapterOpBatchListBinding;
import com.traceabilitysystem.interfaces.OpBatchPrint;
import com.traceabilitysystem.model_api.SettingsApiResponse;
import com.traceabilitysystem.utils.CommonFunctions;
import com.traceabilitysystem.utils._pref.SessionManager;

import java.util.List;

public class OpBatchListAdapter extends RecyclerView.Adapter<OpBatchListAdapter.MyViewHolder> {

    private final Context context;
    private final List<String> data;
    private final List<String> selectedPrint;
    AdapterOpBatchListBinding binding;
    String docNo;
    OpBatchPrint opBatchPrint;

    public OpBatchListAdapter(Context context, List<String> data,String docNo,List<String> selectedPrint,OpBatchPrint opBatchPrint) {
        this.context = context;
        this.data = data;
        this.docNo = docNo;
        this.selectedPrint = selectedPrint;
        this.opBatchPrint = opBatchPrint;
    }

    @NonNull
    @Override
    public OpBatchListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_op_batch_list, parent, false);
        return new OpBatchListAdapter.MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OpBatchListAdapter.MyViewHolder myViewHolder, int position) {

        myViewHolder.txtDocNo.setText(docNo);
        myViewHolder.txtOpBatch.setText(data.get(position));

        if (selectedPrint.size() > 0){
            for (int i = 0; i < selectedPrint.size(); i++) {
                if (selectedPrint.get(i).equals(data.get(position))){
                    myViewHolder.imgTick.setVisibility(View.VISIBLE);
                }
            }
        }

// ---- List Parent Click
        myViewHolder.llyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opBatchPrint.onClick(data.get(position));
            }
        });

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDocNo,txtOpBatch;
        LinearLayout llyParent;
        ImageView imgTick;

        MyViewHolder(@NonNull AdapterOpBatchListBinding itemView) {
            super(itemView.getRoot());

            txtDocNo = itemView.txtDocNo;
            txtOpBatch = itemView.txtOpBatch;
            llyParent = itemView.llyParent;
            imgTick = itemView.imgTick;
        }
    }
}