package tw.sample.samplelinetv.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 手機監測工具
 */
public class NetworkUtil {
    /**
     * 檢測網路連接狀態
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        // 沒有網路 => false
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
