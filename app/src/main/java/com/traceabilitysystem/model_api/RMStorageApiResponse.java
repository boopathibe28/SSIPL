package com.traceabilitysystem.model_api;

public class RMStorageApiResponse {
    private String msg;

    private String code;

    private String status;

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [msg = "+msg+", code = "+code+", status = "+status+"]";
    }
}
