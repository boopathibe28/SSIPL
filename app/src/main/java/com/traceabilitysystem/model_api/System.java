package com.traceabilitysystem.model_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class System {

@SerializedName("nc")
@Expose
private Integer nc;

public Integer getNc() {
return nc;
}

public void setNc(Integer nc) {
this.nc = nc;
}

}