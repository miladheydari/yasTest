package com.yas.features.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.yas.R;
import com.yas.YasTestApplication;
import com.yas.features.musicList.MusicListFragment;
import com.yas.features.musicList.MusicListContract;
import com.yas.features.musicPlayer.MusicPlayerActivity;
import com.yas.pojo.MusicDetail;
import com.yas.utils.ActivityUtils;
import com.yas.utils.PermissionHandler;
import com.yas.utils.bases.BaseActivity;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends BaseActivity implements MusicListContract.View {

    @Inject
    public MusicListContract.Presenter mPresenter;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.llCurrentMusic)
    LinearLayout llCurrentMusic;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvArtist)
    TextView tvArtist;

    MusicListFragment musicListFragment = new MusicListFragment();
    private List<MusicDetail> musicDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mPresenter.onViewAttached(this);

        new DrawerBuilder().withActivity(this).withToolbar(toolbar).build();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        ActivityUtils.addFragmentToActivity(getFragmentManager(), musicListFragment, R.id.flContent);
//                        switch (item.getItemId()) {
//                            case R.id.action_favorites:
//                                textFavorites.setVisibility(View.VISIBLE);
//                                textSchedules.setVisibility(View.GONE);
//                                textMusic.setVisibility(View.GONE);
//                                break;
//                            case R.id.action_schedules:
//                                textFavorites.setVisibility(View.GONE);
//                                textSchedules.setVisibility(View.VISIBLE);
//                                textMusic.setVisibility(View.GONE);
//                                break;
//                            case R.id.action_music:
//                                textFavorites.setVisibility(View.GONE);
//                                textSchedules.setVisibility(View.GONE);
//                                textMusic.setVisibility(View.VISIBLE);
//                                break;
//                        }
                        return true;
                    }
                });
        //FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        new PermissionHandler().checkPermission(MainActivity.this, permissions, new PermissionHandler.OnPermissionResponse() {
            @Override
            public void onPermissionGranted() {

                mPresenter.onViewAttached(MainActivity.this);
                mPresenter.subscribe();
                mPresenter.getMusicList();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(MainActivity.this, "we need permission", Toast.LENGTH_LONG).show();
            }
        });


    }


    private void initCurrentMusic() {
        long currentMusicId = YasTestApplication.getCurrentMusicId();


        if (currentMusicId != -1) {
            MusicDetail currentMusicDetail = musicDetailList.get((int) currentMusicId);
            llCurrentMusic.setVisibility(View.VISIBLE);
            tvArtist.setText(currentMusicDetail.artist);
            tvName.setText(currentMusicDetail.title);
            llCurrentMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("musicList", (Serializable) musicDetailList);
                    startActivity(intent);
                }
            });
        } else llCurrentMusic.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        initCurrentMusic();
        super.onResume();

    }

    @Override
    public void showError() {

    }

    @Override
    public void showMusics(List<MusicDetail> musicDetailList) {
        this.musicDetailList = musicDetailList;
    }
}
