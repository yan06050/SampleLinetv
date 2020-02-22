package tw.sample.samplelinetv.okhttp;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tw.sample.samplelinetv.util.NetworkUtil;
import tw.sample.samplelinetv.util.ShareUtil;
import tw.sample.samplelinetv.R;

public class OkHttpUtil {
    public static void doGet(final Activity activity, String url, final OkHttpCallBack okHttpCallBack) {
        if (!NetworkUtil.isNetworkConnected(activity)) {
            okHttpCallBack.Finish();
            ShareUtil.showAlertDialog(activity, activity.getString(R.string.label_error),
                    activity.getString(R.string.label_not_network), activity.getString(android.R.string.ok));
            return;
        }

        Log.e("OkHttp_doGet", "" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).method("GET", null).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            // 失敗
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            // 成功
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String data = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        okHttpCallBack.Result(data);
                    }
                });
            }
        });
    }

    public static void doPost(final Activity activity, String url, RequestBody formBody, final OkHttpCallBack okHttpCallBack) {
        if (!NetworkUtil.isNetworkConnected(activity)) {
            okHttpCallBack.Finish();
            ShareUtil.showAlertDialog(activity, activity.getString(R.string.label_error),
                    activity.getString(R.string.label_not_network), activity.getString(android.R.string.ok));
            return;
        }

        Log.e("OkHttp_doPost", "" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody formBody = new FormBody.Builder()
//                .add("Type", "iPhone")
//                .add("VersionNo", "2")
//                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String data = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        okHttpCallBack.Result(data);
                    }
                });
            }
        });
    }

}
