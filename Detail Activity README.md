package org.turntotech.samplejson;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends Activity {

    ImageView posterImage;
    TextView movie_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        posterImage = (ImageView)findViewById(R.id.imageView2);
        movie_title = (TextView)findViewById(R.id.textView3);
        String title = getIntent().getStringExtra("title");
        String poster_path = getIntent().getStringExtra("url");
        movie_title.setText(title);

        //GlideApp.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        Glide.with(this)
                .load(poster_path)
                .into(posterImage);

    }
}
