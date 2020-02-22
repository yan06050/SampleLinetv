package tw.sample.samplelinetv.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareUtil {
    public static int vWidth = 0;
    public static int vHeight = 0;
    public static int vHeight_img_item = 0;
    public static int vWidth_img_item = 0;
    public static int vHeight_img_content = 0;

    // 抓取螢幕解析度
    public static void getPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        vWidth = dm.widthPixels;
        vHeight = dm.heightPixels;

        vHeight_img_item = (int) (vHeight * 0.12);
        vWidth_img_item = (int) (vWidth * 0.35);
        vHeight_img_content = (int) (vHeight * 0.35);
    }

    // show Toast
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    // 日期解析
    public static String dateToString(String str) {
        String strDate = "";
        try {
            // "created_at": "2017-12-02T03:34:41.000Z",
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            Date date = sdf.parse(str);
            strDate = date.toString();
            strDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    // 分數 四捨五入
    public static String ratingScale(double rating) {
        BigDecimal bigDec = new BigDecimal(rating);
        double total = bigDec.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(total);
    }

    // 訊息Dialog對話框
    public static void showAlertDialog(Context context, String title, String msg, String positive) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positive, null).show();
    }

    // 跳轉到手機網路畫面
    public static void openRoamingSettings(final Context context, String title, String msg, String positive, String negative) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positive,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                context.startActivity(intent);
                                /**
                                 * ACTION_APN_SETTINGS => APN介面，決定手機通過哪種接入方式來訪問網絡
                                 * ACTION_WIFI_SETTINGS => WIFI介面
                                 * **/
                            }
                        })
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();
    }
}
