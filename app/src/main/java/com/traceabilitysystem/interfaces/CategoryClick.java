package com.traceabilitysystem.interfaces;

import com.traceabilitysystem.model_api.BranchViewApiResponse;
import com.traceabilitysystem.model_api.BranchViewDetailsApiResponse;

import java.util.List;

public interface CategoryClick {
    void onClick(int position, List<BranchViewDetailsApiResponse.Subcategories> subCategoryList);
}
