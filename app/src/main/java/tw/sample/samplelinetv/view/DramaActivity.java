package tw.sample.samplelinetv.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import tw.sample.samplelinetv.R;
import tw.sample.samplelinetv.adapter.DramaAdapter;
import tw.sample.samplelinetv.api.DramaAPI;
import tw.sample.samplelinetv.db.Drama;
import tw.sample.samplelinetv.db.DramaDatabase;
import tw.sample.samplelinetv.model.DramaModel;
import tw.sample.samplelinetv.okhttp.OkHttpCallBack;
import tw.sample.samplelinetv.okhttp.OkHttpUtil;
import tw.sample.samplelinetv.util.NetworkUtil;
import tw.sample.samplelinetv.util.ShareUtil;

public class DramaActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DramaAdapter dramaAdapter;
    // 有網路，撈取API用
    private List<DramaModel.DramaInfo> infos_DramaInfo;
    // 沒有網路，撈取DB資料用
    private List<Drama> infos_Drama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("戲劇列表");
        setSupportActionBar(toolbar);

        initView();
        initEvent();
        getDramaData();

    }

    private void initView() {
        searchView = findViewById(R.id.searchView);
        // 設定該SearchView預設是否自動縮小為圖示
        searchView.setIconifiedByDefault(false);
        // 設定該SearchView顯示搜尋按鈕
        searchView.setSubmitButtonEnabled(true);
        // 為該SearchView元件設定事件監聽器
        searchView.setOnQueryTextListener(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        listView = findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.tv_empty));
        listView.setTextFilterEnabled(true);//設定lv可以被過濾

        dramaAdapter = new DramaAdapter(this, new ArrayList<DramaModel.DramaInfo>());
        listView.setAdapter(dramaAdapter);
        listView.setOnItemClickListener(dramaAdapter);
    }

    private void initEvent() {
        // 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDramaData();
            }
        });

        // 下拉元件的顏色設定
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // get戲劇資訊
    private void getDramaData() {
        // 離線狀態，抓取DramaDatabase 的資料，裡面的資料是最後一次搜尋資料，作離線使用
        if (!NetworkUtil.isNetworkConnected(DramaActivity.this)) {
            ShareUtil.openRoamingSettings(this,
                    getString(R.string.label_not_network),
                    getString(R.string.label_open_roaming_settings),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel));

            /**
             * 離線狀態，撈取資料
             * 撈取DramaDatabase資料，需要在Thread 執行緒裡
             * **/
            new Thread(new Runnable() {

                public void run() {
                    // 抓取 DramaDatabase 資料
                    infos_Drama = DramaDatabase.getInstance(DramaActivity.this).getDramaDao().getAllDramas();
                    // 將 infos_Drama 轉為 json
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Drama>>() {
                    }.getType();
                    String json = gson.toJson(infos_Drama, type);

                    // json 轉為 List
                    infos_DramaInfo = gson.fromJson(json, new TypeToken<List<DramaModel.DramaInfo>>() {
                    }.getType());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            dramaAdapter.updateInfo(infos_DramaInfo);
                            showSwipeRefresh(false);
                        }
                    });
                }
            }).start();
            return;
        }

        // 有網路狀態，撈取API資料
        showSwipeRefresh(true);
        OkHttpUtil.doGet(DramaActivity.this, DramaAPI.getUrl_Dramas(), new OkHttpCallBack() {
            @Override
            public void Result(String response) {
                Log.e("response", "" + response);
                showSwipeRefresh(false);

                DramaModel dramaModel = new Gson().fromJson(response, DramaModel.class);
                infos_DramaInfo = dramaModel.getDramaInfos();
                dramaAdapter.updateInfo(infos_DramaInfo);
                dramaAdapter.setDramaDatabase();
            }

            @Override
            public void Error(String error) {

            }

            @Override
            public void Finish() {
                showSwipeRefresh(false);
            }
        });
    }

    private void showSwipeRefresh(boolean isShow) {
        swipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        dramaAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            dramaAdapter.getFilter().filter(newText);
        }
        return true;
    }

}
