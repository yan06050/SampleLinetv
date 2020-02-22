package tw.sample.samplelinetv.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import tw.sample.samplelinetv.R;
import tw.sample.samplelinetv.model.DramaModel;
import tw.sample.samplelinetv.util.ShareUtil;

public class DramaContentActivity extends AppCompatActivity {
    private ImageView img_drama;
    private TextView tv_name;
    private TextView tv_created_at;
    private TextView tv_total_views;
    private TextView tv_rating;
    private TextView tv_introduction;

    private DramaModel.DramaInfo dramaInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_drama);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("戲劇介紹");
        setSupportActionBar(toolbar);

        dramaInfo = (DramaModel.DramaInfo) getIntent().getSerializableExtra("info");

        initView();

    }

    private void initView() {
        tv_name = findViewById(R.id.tv_name);
        tv_created_at = findViewById(R.id.tv_created_at);
        tv_total_views = findViewById(R.id.tv_total_views);
        tv_rating = findViewById(R.id.tv_rating);
        tv_introduction = findViewById(R.id.tv_introduction);

        tv_name.setText(dramaInfo.getName());
        tv_created_at.setText(ShareUtil.dateToString(dramaInfo.getCreated_at()));
        tv_total_views.setText(String.valueOf(dramaInfo.getTotal_views()));
        tv_rating.setText(String.valueOf(ShareUtil.ratingScale(dramaInfo.getRating())));
        tv_introduction.setText(getString(getDramaIntroduction(dramaInfo.getDrama_id())));

        img_drama = new ImageView(this);
        img_drama.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayout img_content = findViewById(R.id.img_content);
        img_content.addView(img_drama, new RelativeLayout.LayoutParams(
                (int) (ShareUtil.vHeight_img_item * 1.5),
                (int) (ShareUtil.vWidth_img_item * 1.5)));

        Glide.with(this)
                .load(dramaInfo.getThumb())
                .placeholder(R.mipmap.img_default_photo)
                .into(img_drama);
    }

    private Integer getDramaIntroduction(int id) {
        Integer label_Introduction = R.string.label_drama_content;
        switch(id) {
            case 1:
                label_Introduction = R.string.label_introduction_1;
                break;
            case 2:
                label_Introduction = R.string.label_introduction_2;
                break;
            case 3:
                label_Introduction = R.string.label_introduction_3;
                break;
            case 5:
                label_Introduction = R.string.label_introduction_5;
                break;
            case 6:
                label_Introduction = R.string.label_introduction_6;
                break;
            case 10:
                label_Introduction = R.string.label_introduction_10;
                break;
        }
        return label_Introduction;
    }


}
