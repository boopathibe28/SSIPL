package com.traceabilitysystem.api;

import static com.traceabilitysystem.utils._pref.SharedPrefConstants.DEVICE_TOKEN;
import static com.traceabilitysystem.utils._pref.SharedPrefConstants.MOBILE_NO;

import android.content.Context;
import android.widget.Toast;

import com.traceabilitysystem.app_interfaces.CommonCallback;
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
import com.traceabilitysystem.utils.CommonFunctions;
import com.traceabilitysystem.utils.ConnectionErrorDialog;
import com.traceabilitysystem.utils.CustomProgressDialog;
import com.traceabilitysystem.utils.NoInternetConnectionDialog;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommonApiCalls {
    private static final CommonApiCalls ourInstance = new CommonApiCalls();

    private CommonApiCalls() {
    }

    public static CommonApiCalls getInstance() {
        return ourInstance;
    }

// --------POST  Login
    public void login(final Context context, final String mobile_number, String device_token,String device_type_id,
                      final CommonCallback.Listener listener) {
        if (CommonFunctions.getInstance().isNetworkConnected()) {
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(context);
            }
            ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
            Call<LoginApiResponse> call = apiInterface.login(mobile_number, device_token, device_type_id);
            call.enqueue(new Callback<LoginApiResponse>() {
                @Override
                public void onResponse(Call<LoginApiResponse> call, Response<LoginApiResponse> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccess(response.body());
                    } else {
                        listener.onFailure(response.message());
                    }
                    if (CustomProgressDialog.getInstance().isShowing()) {
                        CustomProgressDialog.getInstance().dismiss();
                    }
                }

                @Override
                public void onFailure(Call<LoginApiResponse> call, Throwable t) {
                    CustomProgressDialog.getInstance().dismiss();
                    t.printStackTrace();
                    if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException) {
                        ConnectionErrorDialog.getInstance().showDialog(context);
                    }
                    t.printStackTrace();
                    CommonFunctions.getInstance().apiErrorConverter(context, t.getMessage(), listener);
                }
            });

        }
        else {
            NoInternetConnectionDialog.getInstance().showDialog(context);
        }
    }



/*

    // ------- rm_inventory
    public void rmInventory(final Context context, final String body, final CommonCallback.Listener listener) {
        if (!CustomProgressDialog.getInstance().isShowing()) {
              CustomProgressDialog.getInstance().show(context);
        }
        RequestBody body_ = RequestBody.create(MediaType.parse("text/plain"), body);

        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<RMInventoryApiResponse> call = apiInterface.rm_inventory(body_);
        call.enqueue(new Callback<RMInventoryApiResponse>() {
            @Override
            public void onResponse(Call<RMInventoryApiResponse> call, Response<RMInventoryApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    CommonFunctions.getInstance().validationError(context,"Failed to connect");
                    //CommonFunctions.getInstance().apiErrorConverter(response.errorBody(), listener);
                }
            }

            @Override
            public void onFailure(Call<RMInventoryApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                t.printStackTrace();
                //CommonFunctions.getInstance().apiErrorConverter(t.getMessage(), listener);
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
            }
        });

    }
*/



    // ----- POST - rmInventory
    public void rmInventory(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<RMInventoryApiResponse> call = apiInterface.rm_inventory(body);
        call.enqueue(new Callback<RMInventoryApiResponse>() {
            @Override
            public void onResponse(Call<RMInventoryApiResponse> call, Response<RMInventoryApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                listener.onSuccess(response.body());
                /*if (response.isSuccessful()) {

                } else {
                    CommonFunctions.getInstance().validationError(context,"Failed to connect");
                }*/
            }

            @Override
            public void onFailure(Call<RMInventoryApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                t.printStackTrace();
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
            }
        });
    }

    // ----- POST - ssiplID
    public void ssiplID(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<SsiplIDApiResponse> call = apiInterface.ssipl_id(body);
        call.enqueue(new Callback<SsiplIDApiResponse>() {
            @Override
            public void onResponse(Call<SsiplIDApiResponse> call, Response<SsiplIDApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                listener.onSuccess(response.body());
                /*if (response.isSuccessful()) {

                } else {
                    CommonFunctions.getInstance().validationError(context,"Failed to connect");
                }*/
            }

            @Override
            public void onFailure(Call<SsiplIDApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                t.printStackTrace();
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
            }
        });
    }




    // ----- POST - rmStorage
    public void rmStorage(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<RMStorageApiResponse> call = apiInterface.rm_storage(body);
        call.enqueue(new Callback<RMStorageApiResponse>() {
            @Override
            public void onResponse(Call<RMStorageApiResponse> call, Response<RMStorageApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                listener.onSuccess(response.body());
                /*if (response.isSuccessful()) {

                } else {
                    CommonFunctions.getInstance().validationError(context,"Failed to connect");
                }*/
            }

            @Override
            public void onFailure(Call<RMStorageApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                t.printStackTrace();
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
            }
        });
    }




    // ----- POST - GRN Process Check
    public void GrnProcessCheck(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<GrnProcessCheckApiResponse> call = apiInterface.grn_process(body);
        call.enqueue(new Callback<GrnProcessCheckApiResponse>() {
            @Override
            public void onResponse(Call<GrnProcessCheckApiResponse> call, Response<GrnProcessCheckApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                listener.onSuccess(response.body());
                /*if (response.isSuccessful()) {

                } else {
                    CommonFunctions.getInstance().validationError(context,"Failed to connect");
                }*/
            }

            @Override
            public void onFailure(Call<GrnProcessCheckApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                t.printStackTrace();
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
            }
        });
    }




    // ----- POST - GRN Process Print Bar code
    public void GrnProcessPrintBarCode(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<GrnProcessPrintBarCodeApiResponse> call = apiInterface.grn_process_print_bar_code(body);
        call.enqueue(new Callback<GrnProcessPrintBarCodeApiResponse>() {
            @Override
            public void onResponse(Call<GrnProcessPrintBarCodeApiResponse> call, Response<GrnProcessPrintBarCodeApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<GrnProcessPrintBarCodeApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ----- POST - HRS Slitting
    public void hrsSlitting(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<HrsSlittingApiResponse> call = apiInterface.hrs_slitting(body);
        call.enqueue(new Callback<HrsSlittingApiResponse>() {
            @Override
            public void onResponse(Call<HrsSlittingApiResponse> call, Response<HrsSlittingApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<HrsSlittingApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ----- POST - FG Data
    public void fgData(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<FGDataApiResponse> call = apiInterface.fg_data(body);
        call.enqueue(new Callback<FGDataApiResponse>() {
            @Override
            public void onResponse(Call<FGDataApiResponse> call, Response<FGDataApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<FGDataApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }






    // ----- POST - FG Data Check
    public void fgDataCheck(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<FGDataCheckApiResponse> call = apiInterface.fg_data_check(body);
        call.enqueue(new Callback<FGDataCheckApiResponse>() {
            @Override
            public void onResponse(Call<FGDataCheckApiResponse> call, Response<FGDataCheckApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<FGDataCheckApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }





    // ----- POST - Sales Return Process Check
    public void salesReturnCheck(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<SalesReturnCheckApiResponse> call = apiInterface.sales_return(body);
        call.enqueue(new Callback<SalesReturnCheckApiResponse>() {
            @Override
            public void onResponse(Call<SalesReturnCheckApiResponse> call, Response<SalesReturnCheckApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                listener.onSuccess(response.body());
                /*if (response.isSuccessful()) {

                } else {
                    CommonFunctions.getInstance().validationError(context,"Failed to connect");
                }*/
            }

            @Override
            public void onFailure(Call<SalesReturnCheckApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                t.printStackTrace();
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
            }
        });
    }




    // ----- POST - Sales Return Print Bar code
    public void salesReturnPrintBarCode(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<SalesReturnPrintBarCodeApiResponse> call = apiInterface.sales_return_print_bar_code(body);
        call.enqueue(new Callback<SalesReturnPrintBarCodeApiResponse>() {
            @Override
            public void onResponse(Call<SalesReturnPrintBarCodeApiResponse> call, Response<SalesReturnPrintBarCodeApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<SalesReturnPrintBarCodeApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ----- GET Data - Fg_Scrab
    public void timmedScrapGetData(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<TrimmedScrapApiResponse> call = apiInterface.trimmed_scrap(body);
        call.enqueue(new Callback<TrimmedScrapApiResponse>() {
            @Override
            public void onResponse(Call<TrimmedScrapApiResponse> call, Response<TrimmedScrapApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<TrimmedScrapApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }



    // ----- POST Roll Back - End Cut
    public void recPostApiData(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<RoolbackEndcutApiResponse> call = apiInterface.roolback_endcut(body);
        call.enqueue(new Callback<RoolbackEndcutApiResponse>() {
            @Override
            public void onResponse(Call<RoolbackEndcutApiResponse> call, Response<RoolbackEndcutApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<RoolbackEndcutApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }



    // ----- POST Trimmed Scrap Data
    public void postTrimmedScrap(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<TrimmedScrapPostApiResponse> call = apiInterface.post_trimmed_scrap(body);
        call.enqueue(new Callback<TrimmedScrapPostApiResponse>() {
            @Override
            public void onResponse(Call<TrimmedScrapPostApiResponse> call, Response<TrimmedScrapPostApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<TrimmedScrapPostApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }





    // ----- POST Rollback End cut
    public void rollbackEndCutWorkOrder(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<RollbackEndCutWorkOrder> call = apiInterface.rollback_end_cut(body);
        call.enqueue(new Callback<RollbackEndCutWorkOrder>() {
            @Override
            public void onResponse(Call<RollbackEndCutWorkOrder> call, Response<RollbackEndCutWorkOrder> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<RollbackEndCutWorkOrder> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }





    // ----- POST Re-Print RM Inward
    public void rmInward(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<ReprintApiResponse> call = apiInterface.reprint(body);
        call.enqueue(new Callback<ReprintApiResponse>() {
            @Override
            public void onResponse(Call<ReprintApiResponse> call, Response<ReprintApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<ReprintApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }



    // ----- POST Re-Print WIP
    public void wipCall(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<WipApiResponse> call = apiInterface.wip(body);
        call.enqueue(new Callback<WipApiResponse>() {
            @Override
            public void onResponse(Call<WipApiResponse> call, Response<WipApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<WipApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ----- POST - WIP Store
    public void wipStore(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<WIPStorageApiResponse> call = apiInterface.wip_storage(body);
        call.enqueue(new Callback<WIPStorageApiResponse>() {
            @Override
            public void onResponse(Call<WIPStorageApiResponse> call, Response<WIPStorageApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                listener.onSuccess(response.body());
                /*if (response.isSuccessful()) {

                } else {
                    CommonFunctions.getInstance().validationError(context,"Failed to connect");
                }*/
            }

            @Override
            public void onFailure(Call<WIPStorageApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                t.printStackTrace();
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
            }
        });
    }


    // ------- POST - FgDispatch
    public void fgDispatch(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<FgDispatchApiResponse> call = apiInterface.fg_dispatch(body);
        call.enqueue(new Callback<FgDispatchApiResponse>() {
            @Override
            public void onResponse(Call<FgDispatchApiResponse> call, Response<FgDispatchApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<FgDispatchApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }



    // ------- POST - Load Data to  FgDispatch
    public void fgDispatchLoad(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<FgDispatchLoadApiResponse> call = apiInterface.fg_dispatch_load(body);
        call.enqueue(new Callback<FgDispatchLoadApiResponse>() {
            @Override
            public void onResponse(Call<FgDispatchLoadApiResponse> call, Response<FgDispatchLoadApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<FgDispatchLoadApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ------- POST - Query
    public void query(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<QueryApiResponse> call = apiInterface.query(body);
        call.enqueue(new Callback<QueryApiResponse>() {
            @Override
            public void onResponse(Call<QueryApiResponse> call, Response<QueryApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<QueryApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ------- POST - Add Prime
    public void addPrime(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<AddPrimeApiResponse> call = apiInterface.add_prime(body);
        call.enqueue(new Callback<AddPrimeApiResponse>() {
            @Override
            public void onResponse(Call<AddPrimeApiResponse> call, Response<AddPrimeApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<AddPrimeApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ------- POST - Delete Prime GET
    public void deletePrimeGet(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<DeletePrimeGetApiResponse> call = apiInterface.delete_prime_get(body);
        call.enqueue(new Callback<DeletePrimeGetApiResponse>() {
            @Override
            public void onResponse(Call<DeletePrimeGetApiResponse> call, Response<DeletePrimeGetApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<DeletePrimeGetApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ------- POST - Delete Prime DELETE CALL
    public void deletePrimeDelete(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<DeletePrimeDeleteApiResponse> call = apiInterface.delete_prime_delete(body);
        call.enqueue(new Callback<DeletePrimeDeleteApiResponse>() {
            @Override
            public void onResponse(Call<DeletePrimeDeleteApiResponse> call, Response<DeletePrimeDeleteApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<DeletePrimeDeleteApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ------- GET - Op Batch List
    public void getOpBatchList(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<OpBatchListApiResponse> call = apiInterface.get_op_batch(body);
        call.enqueue(new Callback<OpBatchListApiResponse>() {
            @Override
            public void onResponse(Call<OpBatchListApiResponse> call, Response<OpBatchListApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<OpBatchListApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }





    // ------- GET - Printing List
    public void printingList(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<PrintingListApiResponse> call = apiInterface.get_printing(body);
        call.enqueue(new Callback<PrintingListApiResponse>() {
            @Override
            public void onResponse(Call<PrintingListApiResponse> call, Response<PrintingListApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<PrintingListApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }




    // ------- GET - Business Type List
    public void business_type(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<BusinessTypeListApiResponse> call = apiInterface.get_business_type(body);
        call.enqueue(new Callback<BusinessTypeListApiResponse>() {
            @Override
            public void onResponse(Call<BusinessTypeListApiResponse> call, Response<BusinessTypeListApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<BusinessTypeListApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }



    // ------- GET - Op batch Correction
    public void opBatchDetails(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<OPBatchDetailsApiResponse> call = apiInterface.get_opbatch_detail(body);
        call.enqueue(new Callback<OPBatchDetailsApiResponse>() {
            @Override
            public void onResponse(Call<OPBatchDetailsApiResponse> call, Response<OPBatchDetailsApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<OPBatchDetailsApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }



    // ------- GET - Op batch Correction Print
    public void opBatchPrint(final Context context,String body, final CommonCallback.Listener listener) {

        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(context);
        }
        ApiInterface apiInterface = ApiConfiguration.getInstance().getApiBuilder().create(ApiInterface.class);
        Call<OPBatchPrintApiResponse> call = apiInterface.get_opbatch_print(body);
        call.enqueue(new Callback<OPBatchPrintApiResponse>() {
            @Override
            public void onResponse(Call<OPBatchPrintApiResponse> call, Response<OPBatchPrintApiResponse> response) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(response.message());
                    CommonFunctions.getInstance().validationError(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<OPBatchPrintApiResponse> call, Throwable t) {
                if (CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().dismiss();
                }
                CommonFunctions.getInstance().validationError(context,"Failed to connect");
                t.printStackTrace();
            }
        });
    }



}
