package com.traceabilitysystem.interfaces;


import com.traceabilitysystem.model_api.CustomerAddressListApiResponse;

public interface AddressListClick {
    void onClicEdit(CustomerAddressListApiResponse.Datum datum);
    void onClickDelete(String key);
    void onAddressSelect(CustomerAddressListApiResponse.Datum address);
}
