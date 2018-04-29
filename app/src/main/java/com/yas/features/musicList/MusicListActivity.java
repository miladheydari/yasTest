package com.yas.features.musicList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.squareup.picasso.Picasso;
import com.yas.R;
import com.yas.YasTestApplication;
import com.yas.features.musicPlayer.MusicPlayerActivity;
import com.yas.pojo.MusicDetail;
import com.yas.utils.bases.BaseActivity;
import com.yas.utils.customview.LoadingLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

import static android.R.attr.id;

public class MusicListActivity extends BaseActivity implements MusicListContract.View {


    @Inject
    public MusicListContract.Presenter mPresenter;
    @Inject
    Picasso picasso;

    private List<MusicDetail> musicDetails;

    @BindView(R.id.loading)
    LoadingLayout loading;


    MusicListAdapter mAdapter;
    @BindView(R.id.rvCat)
    RecyclerView rvCat;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.llCurrentMusic)
    LinearLayout llCurrentMusic;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvArtist)
    TextView tvArtist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mPresenter.onViewAttached(this);

        new DrawerBuilder().withActivity(this).withToolbar(toolbar).build();

//        if (savedInstanceState != null) {
//            // Restore value of members from saved state
//            id = savedInstanceState.getInt("save_state_video_id");
//            name = savedInstanceState.getString("save_state_name");
//            video_url = savedInstanceState.getString("save_state_video_url");
//        }
//
//
//        if(getIntent().getExtras()!=null)
//        {
//            id=getIntent().getIntExtra("video_id",-1);
//            name=getIntent().getStringExtra("video_name");
//        }


        initRecyclerview();
        initCurrentMusic();


        mPresenter.subscribe();
//        mPresenter.getVideoDetailById();
        mPresenter.getMusicList();
    }

    private void initCurrentMusic() {
        MusicDetail currentMusicDetail = YasTestApplication.getCurrentMusic();
        if (YasTestApplication.getCurrentMusic() != null) {
            llCurrentMusic.setVisibility(View.VISIBLE);
            tvArtist.setText(currentMusicDetail.artist);
            tvName.setText(currentMusicDetail.title);
        } else llCurrentMusic.setVisibility(View.GONE);
    }


    public void initRecyclerview() {
        musicDetails = new ArrayList<>();
        mAdapter = new MusicListAdapter(this, picasso, musicDetails);
        rvCat.setLayoutManager(new GridLayoutManager(this, 3));

//        rvCat.setLayoutManager(new LinearLayoutManager(MusicListActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvCat.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, MusicDetail video) {
                Intent intent = new Intent(MusicListActivity.this, MusicPlayerActivity.class);
//                Parcels.wrap
                intent.putExtra("musicList", (Serializable) musicDetails);
                intent.putExtra("currentPosition", position);
                startActivity(intent);
            }
        });
    }


    @Override
    public void showError() {
        loading.setState(LoadingLayout.STATE_SHOW_Error);

    }

    @Override
    protected void onResume() {
        initCurrentMusic();
        super.onResume();

    }

    @Override
    public void showMusics(List<MusicDetail> musicDetailList) {
        musicDetails.clear();
        musicDetails.addAll(musicDetailList);

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
