package tw.sample.samplelinetv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import tw.sample.samplelinetv.util.ShareUtil;
import tw.sample.samplelinetv.view.DramaActivity;
import tw.sample.samplelinetv.view.DramaActivity_rv;
import tw.sample.samplelinetv.view.DramaContentActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("選擇呈現方式");
        setSupportActionBar(toolbar);
        ShareUtil.getPixels(this);

    }

    public void openList(View view) {
        Intent intent = new Intent(this, DramaActivity.class);
        startActivity(intent);
    }

    public void openGrid(View view) {
        Intent intent = new Intent(this, DramaActivity_rv.class);
        startActivity(intent);
    }

}
