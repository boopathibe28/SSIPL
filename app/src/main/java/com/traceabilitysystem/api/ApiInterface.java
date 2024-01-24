package com.traceabilitysystem.api;

import com.traceabilitysystem.dummy_model.AddPrimeApiResponse;
import com.traceabilitysystem.dummy_model.BusinessTypeListApiResponse;
import com.traceabilitysystem.dummy_model.DeletePrimeDeleteApiResponse;
import com.traceabilitysystem.dummy_model.DeletePrimeGetApiResponse;
import com.traceabilitysystem.dummy_model.FGDataCheckApiResponse;
import com.traceabilitysystem.dummy_model.FgDispatchApiResponse;
import com.traceabilitysystem.dummy_model.FgDispatchLoadApiResponse;
import com.traceabilitysystem.dummy_model.OPBatchDetailsApiResponse;
import com.traceabilitysystem.dummy_model.OPBatchPrintApiResponse;
import com.traceabilitysystem.dummy_model.OpBatchListApiResponse;
import com.traceabilitysystem.dummy_model.PrintingListApiResponse;
import com.traceabilitysystem.dummy_model.QueryApiResponse;
import com.traceabilitysystem.model_api.FGDataApiResponse;
import com.traceabilitysystem.model_api.GrnProcessCheckApiResponse;
import com.traceabilitysystem.model_api.GrnProcessPrintBarCodeApiResponse;
import com.traceabilitysystem.model_api.HrsSlittingApiResponse;
import com.traceabilitysystem.model_api.LoginApiResponse;
import com.traceabilitysystem.model_api.RMInventoryApiResponse;
import com.traceabilitysystem.model_api.RMStorageApiResponse;
import com.traceabilitysystem.model_api.ReprintApiResponse;
import com.traceabilitysystem.model_api.RollbackEndCutWorkOrder;
import com.traceabilitysystem.model_api.RoolbackEndcutApiResponse;
import com.traceabilitysystem.model_api.SalesReturnCheckApiResponse;
import com.traceabilitysystem.model_api.SalesReturnPrintBarCodeApiResponse;
import com.traceabilitysystem.model_api.SsiplIDApiResponse;
import com.traceabilitysystem.model_api.TrimmedScrapApiResponse;
import com.traceabilitysystem.model_api.TrimmedScrapPostApiResponse;
import com.traceabilitysystem.model_api.WIPStorageApiResponse;
import com.traceabilitysystem.model_api.WipApiResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {
// ---- Login
    @FormUrlEncoded
    @POST(Urls.LOGIN)
    Call<LoginApiResponse> login(@Field("mobile_number") String mobile_number,
                                 @Field("device_token") String device_token,
                                 @Field("device_type_id") String device_type_id);



/*
// ---- RM_INVENTORY
    @POST(Urls.RM_INVENTORY)
    Call<RMInventoryApiResponse> rm_inventory(@Body RequestBody body);
*/

    // ----RM_INVENTORY
    @FormUrlEncoded
    @POST(Urls.RM_INVENTORY)
    Call<RMInventoryApiResponse> rm_inventory(@Field("data") String data);


    // ----SSIPL_ID
    @FormUrlEncoded
    @POST(Urls.SSIPL_ID)
    Call<SsiplIDApiResponse> ssipl_id(@Field("data") String data);

    // ----RM_STORAGE
    @FormUrlEncoded
    @POST(Urls.RM_STORAGE)
    Call<RMStorageApiResponse> rm_storage(@Field("data") String data);

    // ----GRN Process CHECK
    @FormUrlEncoded
    @POST(Urls.GRN_PROCESS)
    Call<GrnProcessCheckApiResponse> grn_process(@Field("data") String data);

    // ----GRN Process Print Bar Code
    @FormUrlEncoded
    @POST(Urls.GRN_PROCESS)
    Call<GrnProcessPrintBarCodeApiResponse> grn_process_print_bar_code(@Field("data") String data);



    // ----HRS Slitting
    @FormUrlEncoded
    @POST(Urls.HRS_SLITTING)
    Call<HrsSlittingApiResponse> hrs_slitting(@Field("data") String data);


    // ----FG_DATA
    @FormUrlEncoded
    @POST(Urls.FG_DATA)
    Call<FGDataApiResponse> fg_data(@Field("data") String data);


    // ----Sales Return CHECK
    @FormUrlEncoded
    @POST(Urls.SALES_RETURN)
    Call<SalesReturnCheckApiResponse> sales_return(@Field("data") String data);


    // ----GRN Sales Return Print Bar Code
    @FormUrlEncoded
    @POST(Urls.SALES_RETURN)
    Call<SalesReturnPrintBarCodeApiResponse> sales_return_print_bar_code(@Field("data") String data);


    // ----GET Data Trimmed Scrap
    @FormUrlEncoded
    @POST(Urls.TRIMMED_SCRAP)
    Call<TrimmedScrapApiResponse> trimmed_scrap(@Field("data") String data);


    // ----POST Roolback_Endcut
    @FormUrlEncoded
    @POST(Urls.ROOLBACK_ENDCUT)
    Call<RoolbackEndcutApiResponse> roolback_endcut(@Field("data") String data);


    // ----POST Data Trimmed Scrap
    @FormUrlEncoded
    @POST(Urls.TRIMMED_SCRAP)
    Call<TrimmedScrapPostApiResponse> post_trimmed_scrap(@Field("data") String data);



    // ----POST Rollback End Cut Work Order
    @FormUrlEncoded
    @POST(Urls.ROOLBACK_ENDCUT)
    Call<RollbackEndCutWorkOrder> rollback_end_cut(@Field("data") String data);


    // ----POST RE-PRINT RM_Inward
    @FormUrlEncoded
    @POST(Urls.REPRINT)
    Call<ReprintApiResponse> reprint(@Field("data") String data);


    // ----POST RE-PRINT Wip
    @FormUrlEncoded
    @POST(Urls.REPRINT)
    Call<WipApiResponse> wip(@Field("data") String data);


    // ----WIP_STORAGE
    @FormUrlEncoded
    @POST(Urls.WIP_STORAGE)
    Call<WIPStorageApiResponse> wip_storage(@Field("data") String data);



    // ----FG_DATA Check
    @FormUrlEncoded
    @POST(Urls.FG_DATA)
    Call<FGDataCheckApiResponse> fg_data_check(@Field("data") String data);


    // ----POST DISPATCH
    @FormUrlEncoded
    @POST(Urls.DISPATCH)
    Call<FgDispatchApiResponse> fg_dispatch(@Field("data") String data);


    // ----POST DISPATCH
    @FormUrlEncoded
    @POST(Urls.DISPATCH)
    Call<FgDispatchLoadApiResponse> fg_dispatch_load(@Field("data") String data);


    // ----POST QUERY
    @FormUrlEncoded
    @POST(Urls.QUERY)
    Call<QueryApiResponse> query(@Field("data") String data);

    // ----ADD_PRIME
    @FormUrlEncoded
    @POST(Urls.ADD_PRIME)
    Call<AddPrimeApiResponse> add_prime(@Field("data") String data);


    // ----POST Delete Prime GET
    @FormUrlEncoded
    @POST(Urls.DELETE_PRIME_GET)
    Call<DeletePrimeGetApiResponse> delete_prime_get(@Field("data") String data);

    // ----POST Delete Prime DELETE Call
    @FormUrlEncoded
    @POST(Urls.DELETE_PRIME_DELETE)
    Call<DeletePrimeDeleteApiResponse> delete_prime_delete(@Field("data") String data);

    // ----GET OP Batch List
    @FormUrlEncoded
    @POST(Urls.PRINT)
    Call<OpBatchListApiResponse> get_op_batch(@Field("data") String data);

    // ----GET Printing List
    @FormUrlEncoded
    @POST(Urls.PRINT)
    Call<PrintingListApiResponse> get_printing(@Field("data") String data);


    // ----GET Business Type List
    @FormUrlEncoded
    @POST(Urls.PRINT)
    Call<BusinessTypeListApiResponse> get_business_type(@Field("data") String data);


    // ----GET OP BATCH CORRECTION
    @FormUrlEncoded
    @POST(Urls.PRINT)
    Call<OPBatchDetailsApiResponse> get_opbatch_detail(@Field("data") String data);


    // ----GET OP BATCH CORRECTION PRINT
    @FormUrlEncoded
    @POST(Urls.PRINT)
    Call<OPBatchPrintApiResponse> get_opbatch_print(@Field("data") String data);


}
