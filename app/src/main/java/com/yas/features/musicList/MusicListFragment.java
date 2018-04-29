package com.yas.features.musicList;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yas.R;
import com.yas.features.musicPlayer.MusicPlayerActivity;
import com.yas.pojo.MusicDetail;
import com.yas.utils.PermissionHandler;
import com.yas.utils.customview.LoadingLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MusicListFragment extends Fragment implements MusicListContract.View {


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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        View root = inflater.inflate(R.layout.activity_music_list, container, false);
        ButterKnife.bind(this, root);
        mPresenter.onViewAttached(this);

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



        mPresenter.subscribe();
//        mPresenter.getVideoDetailById();
        mPresenter.getMusicList();
        return root;
    }




    public void initRecyclerview() {
        musicDetails = new ArrayList<>();
        mAdapter = new MusicListAdapter(getActivity(), picasso, musicDetails);
        rvCat.setLayoutManager(new GridLayoutManager(getActivity(), 3));

//        rvCat.setLayoutManager(new LinearLayoutManager(MusicListFragment.this, LinearLayoutManager.HORIZONTAL, false));
        rvCat.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, MusicDetail video) {
                Intent intent = new Intent(getActivity(), MusicPlayerActivity.class);
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

}
