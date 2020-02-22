package tw.sample.samplelinetv.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import tw.sample.samplelinetv.view.DramaContentActivity;
import tw.sample.samplelinetv.R;
import tw.sample.samplelinetv.db.Drama;
import tw.sample.samplelinetv.db.DramaDatabase;
import tw.sample.samplelinetv.model.DramaModel;
import tw.sample.samplelinetv.util.NetworkUtil;
import tw.sample.samplelinetv.util.ShareUtil;

public class DramaAdapter_rv extends RecyclerView.Adapter<DramaAdapter_rv.ViewHolder> implements Filterable {
    private Context context;
//    private boolean isList = false;
    private List<DramaModel.DramaInfo> infos_DramaInfo;
    private List<DramaModel.DramaInfo> infos_Original;

    public DramaAdapter_rv(Context context, List<DramaModel.DramaInfo> infos_DramaInfo) {
        this.context = context;
        this.infos_DramaInfo = infos_DramaInfo;
    }

    public void updateInfo(List<DramaModel.DramaInfo> infos_DramaInfo) {
        this.infos_DramaInfo = infos_DramaInfo;
        this.notifyDataSetChanged();
    }

    @Override
    public DramaAdapter_rv.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drama_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DramaAdapter_rv.ViewHolder holder, final int position) {
        final DramaModel.DramaInfo info = infos_DramaInfo.get(position);

        holder.tv_name.setText(info.getName());
        holder.tv_created_at.setText(ShareUtil.dateToString(info.getCreated_at()));
        holder.tv_rating.setText(String.valueOf(ShareUtil.ratingScale(info.getRating())));

        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayout img_content = holder.img_content.findViewById(R.id.img_content);
        img_content.addView(img);

        Glide.with(context)
                .load(info.getThumb())
                .placeholder(R.mipmap.img_default_photo)
                .into(img);

        holder.img_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DramaModel.DramaInfo info = infos_DramaInfo.get(position);

                Intent intent = new Intent(context, DramaContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", info);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infos_DramaInfo.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString();
                FilterResults result = new FilterResults();
                if (infos_Original == null) {
                    synchronized (this) {
                        infos_Original = new ArrayList<>(infos_DramaInfo);
                        // infos_Original 沒有資料，會複製一份 infos_DramaInfo 的過來.
                    }
                }
                if (constraint != null && constraint.toString().length() > 0) {
                    List<DramaModel.DramaInfo> filteredItem = new ArrayList<>();
                    for (int i = 0; i < infos_Original.size(); i++) {
                        String name = infos_Original.get(i).getName();
                        if (name.contains(constraint)) {
                            filteredItem.add(infos_Original.get(i));
                        }
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                } else {
                    synchronized (this) {
                        List<DramaModel.DramaInfo> list = new ArrayList<>(infos_Original);
                        result.values = list;
                        result.count = list.size();
                    }
                }

                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                infos_DramaInfo = (List<DramaModel.DramaInfo>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                    setDramaDatabase();
                }
            }
        };
        return filter;
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout img_content;
        TextView tv_name, tv_rating, tv_created_at;

        ViewHolder(View itemView) {
            super(itemView);
            img_content = itemView.findViewById(R.id.img_content);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_created_at = itemView.findViewById(R.id.tv_created_at);
        }
    }

    /**
     * 保留畫面最後一次顯示的資料去存進 Database
     * 儲存的資料於離線的時候使用
     */
    public void setDramaDatabase() {
        /**
         * 在離線狀態時不做 Database 的更新
         * 所以沒有網路時，return
         * **/
        if (!NetworkUtil.isNetworkConnected(context)) {
            return;
        }

        /**
         * 有連接網路狀態時去做搜尋，範例：搜尋關鍵字 : 好
         * 畫面會顯示兩筆資訊 : 致我們單純的小美好、如果有妹妹就好了
         * 當下的畫面 滑開APP，以離線狀態進入就會是此兩筆資訊
         * */
        // 有網路狀態時，會記錄畫面最後一次的資料
        new Thread(new Runnable() {
            public void run() {
                // 每次寫入資料時，記得先清空Database
                DramaDatabase.getInstance(context).clearAllTables();

                // 清空之後，開始寫入這次的搜尋資料
                for (DramaModel.DramaInfo info : infos_DramaInfo) {
                    Drama drama = new Drama();
                    drama.setDrama_id(info.getDrama_id());
                    drama.setName(info.getName());
                    drama.setTotal_views(info.getTotal_views());
                    drama.setCreated_at(info.getCreated_at());
                    drama.setThumb(info.getThumb());
                    drama.setRating(info.getRating());
                    DramaDatabase.getInstance(context).getDramaDao().insert(drama);
                }
            }
        }).start();
    }
}
