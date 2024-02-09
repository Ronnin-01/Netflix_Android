package com.application.netflix.Mainscreens;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.application.netflix.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class VideoPlayer extends AppCompatActivity {
   private ExoPlayer player;
   private StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setUpExoplayer(getIntent().getStringExtra("url"));
        playerView = findViewById(R.id.exoplayer);
        playerView.setPlayer(player);

    }
    private void setUpExoplayer(String url) {
        player = new ExoPlayer.Builder(VideoPlayer.this).build();

        DataSource.Factory dataSourceFactory=new DefaultDataSourceFactory(this, Util.getUserAgent(this,"Netflix"));
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)));
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);
    }
    protected void onDestroy(){
        super.onDestroy();
        player.release();
    }
}