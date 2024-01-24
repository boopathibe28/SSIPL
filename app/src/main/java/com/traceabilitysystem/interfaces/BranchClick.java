package com.traceabilitysystem.interfaces;


import com.traceabilitysystem.model_api.BranchListApiResponse;

public interface BranchClick {
    void onClick(int position, BranchListApiResponse.Lists response);
    void onClickFav(int position);
}
