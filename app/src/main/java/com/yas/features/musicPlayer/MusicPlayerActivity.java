package com.yas.features.musicPlayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yas.R;
import com.yas.YasTestApplication;
import com.yas.pojo.MusicDetail;
import com.yas.utils.bases.BaseActivity;
import com.yas.utils.customview.LoadingLayout;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import info.abdolahi.CircularMusicProgressBar;
import info.abdolahi.OnCircularSeekBarChangeListener;
import timber.log.Timber;

import static android.R.attr.id;

public class MusicPlayerActivity extends BaseActivity implements MusicPlayerContract.View {


    @Inject
    public MusicPlayerContract.Presenter mPresenter;
    @Inject
    Picasso picasso;
    @Inject
    MediaPlayer mMediaPlayer;
    private List<MusicDetail> musicDetails;

    @BindView(R.id.loading)
    LoadingLayout loading;

    @BindView(R.id.tvMusicName)
    TextView tvMusicName;
    @BindView(R.id.tvArtist)
    TextView tvArtist;

    @BindView(R.id.imgNext)
    ImageView imgNext;
    @BindView(R.id.imgPrevious)
    ImageView imgPrevious;
    @BindView(R.id.pbMusic)
    CircularMusicProgressBar pbMusic;

    @BindView(R.id.imgPlay)
    ImageView imgPlay;
    @BindView(R.id.imgRepeat)
    ImageView imgRepeat;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int positionToPlay;
    Handler mSeekbarUpdateHandler = new Handler();
    private boolean flagPlay = true;
    private Runnable mUpdateSeekbar;


    @OnClick(R.id.imgPrevious)
    void onClickPrevius() {
        if (positionToPlay != 0) {
            positionToPlay--;
            try {
                playMusic(musicDetails.get(positionToPlay));
            } catch (IOException e) {
                Timber.d(e);
            }
        }
    }
reza.safdari@g  
    @OnClick(R.id.imgNext)
    void onClickNext() {
        if (positionToPlay != musicDetails.size() - 1) {
            positionToPlay++;
            try {
                playMusic(musicDetails.get(positionToPlay));
            } catch (IOException e) {
                Timber.d(e);
            }
        }
    }


    @OnClick(R.id.imgPlay)
    void onClickPlay() {
        if (flagPlay) {
            mMediaPlayer.pause();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            flagPlay = false;
            imgPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
            mMediaPlayer.start();
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            flagPlay = true;
            imgPlay.setImageResource(R.drawable.ic_pause_black_24dp);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
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

        initMusic();
        initToolbar();
        mPresenter.subscribe();
//        mPresenter.getVideoDetailById();
//        mPresenter.getMusicList();
    }

    private void initMusic() {
//        Parcels.unwrap
        musicDetails = (List<MusicDetail>) getIntent().getSerializableExtra("musicList");
        positionToPlay = getIntent().getIntExtra("currentPosition", 0);
        pbMusic.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {
                if (fromUser)
                    mMediaPlayer.seekTo(progress);
            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {

            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {

            }
        });

        try {
            playMusic(musicDetails.get(positionToPlay));
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        } catch (IOException e) {
            Timber.d(e);
        }

    }


    private void playMusic(MusicDetail musicDetail) throws IOException {

        mMediaPlayer.reset();

        mMediaPlayer.setDataSource(musicDetail.musicPath);
        //mMediaPlayer.setLooping(true);
        mMediaPlayer.prepare();

        YasTestApplication.setCurrentMusicPlay(musicDetail);
        mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                pbMusic.setValueWithNoAnimation((float) (100 * (1.0 * mMediaPlayer.getCurrentPosition() / mMediaPlayer.getDuration())));
                mSeekbarUpdateHandler.postDelayed(this, 50);
            }
        };

        tvArtist.setText(musicDetail.artist);
        tvMusicName.setText(musicDetail.title);

        picasso.load(musicDetail.imagePath).noFade().into(pbMusic);

        mMediaPlayer.start();

    }


    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }




    @Override
    public void showError() {
        loading.setState(LoadingLayout.STATE_SHOW_Error);

    }

    @Override
    public void showMusics(List<MusicDetail> musicDetailList) {

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
