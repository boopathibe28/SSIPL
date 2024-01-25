package com.traceabilitysystem.dummy_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OPBatchPrintJSON {
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("edit")
    @Expose
    private String edit;
    @SerializedName("gross_weight")
    @Expose
    private float gross_weight;
    @SerializedName("customer_name")
    @Expose
    private String customer_name;
    @SerializedName("thick")
    @Expose
    private float thick;
    @SerializedName("width")
    @Expose
    private float width;
    @SerializedName("length")
    @Expose
    private float length;
    @SerializedName("customer_code")
    @Expose
    private String customer_code;
    @SerializedName("op_batch")
    @Expose
    private String op_batch;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }


    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getOp_batch() {
        return op_batch;
    }

    public void setOp_batch(String op_batch) {
        this.op_batch = op_batch;
    }


    public float getGross_weight() {
        return gross_weight;
    }

    public void setGross_weight(float gross_weight) {
        this.gross_weight = gross_weight;
    }

    public float getThick() {
        return thick;
    }

    public void setThick(float thick) {
        this.thick = thick;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
