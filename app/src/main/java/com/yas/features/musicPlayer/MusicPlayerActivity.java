package com.yas.features.musicPlayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yas.R;
import com.yas.YasTestApplication;
import com.yas.interactors.MusicService;
import com.yas.pojo.MusicDetail;
import com.yas.utils.ListOrderType;
import com.yas.utils.bases.BaseActivity;
import com.yas.utils.customview.LoadingLayout;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import info.abdolahi.CircularMusicProgressBar;
import info.abdolahi.OnCircularSeekBarChangeListener;
import jp.wasabeef.blurry.Blurry;
import timber.log.Timber;

public class MusicPlayerActivity extends BaseActivity implements MusicPlayerContract.View, ServiceConnection {


    @Inject
    public MusicPlayerContract.Presenter mPresenter;
    @Inject
    Picasso picasso;
    @Inject
    SharedPreferences sharedPreferences;

    MediaPlayer mMediaPlayer;
    private List<MusicDetail> musicDetails;
    private ListOrderType listOrderType;

    @BindView(R.id.loading)
    LoadingLayout loading;

    @BindView(R.id.imgFill)
    ImageView imgFill;

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
    ImageButton imgPlay;
    @BindView(R.id.imgRepeat)
    ImageButton imgRepeat;

    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    private int positionToPlay;
    Handler mSeekbarUpdateHandler = new Handler();

    private Runnable mUpdateSeekbar;


    @OnClick(R.id.imgPrevious)
    void onClickPrevius() {
        if (positionToPlay != 0)
            positionToPlay--;
        else positionToPlay = musicDetails.size() - 1;
        try {
            playMusic((positionToPlay));
        } catch (IOException e) {
            Timber.d(e);
        }

    }

    @OnClick(R.id.imgNext)
    void onClickNext() {
        if (positionToPlay != musicDetails.size() - 1)
            positionToPlay++;
        else positionToPlay = 0;
        try {
            playMusic((positionToPlay));
        } catch (IOException e) {
            Timber.d(e);
        }

    }


    @OnClick(R.id.imgPlay)
    void onClickPlay() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);

            imgPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
            mMediaPlayer.start();
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);

            imgPlay.setImageResource(R.drawable.ic_pause_black_24dp);

        }
    }

    @OnClick(R.id.imgRepeat)
    void onClickRepeat() {
        if (listOrderType == ListOrderType.ONE_REPEAT) {
            listOrderType = ListOrderType.REPEAT;
            imgRepeat.setImageResource(R.drawable.ic_repeat_black_24dp);
        } else if (listOrderType == ListOrderType.REPEAT) {
            listOrderType = ListOrderType.SHUFFLE;
            imgRepeat.setImageResource(R.drawable.ic_shuffle_black_24dp);
        } else if (listOrderType == ListOrderType.SHUFFLE) {
            listOrderType = ListOrderType.ONE_REPEAT;
            imgRepeat.setImageResource(R.drawable.ic_repeat_one_black_24dp);
        }
        sharedPreferences.edit().putString("listOrderType", listOrderType.name()).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
        mPresenter.onViewAttached(this);
//        Intent service = new Intent(this, MusicService.class);
//        startService(service);

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

//        initToolbar();
        mPresenter.subscribe();
//        mPresenter.getVideoDetailById();
//        mPresenter.getMusicList();
    }


    private void initMusic() {
//        Parcels.unwrap
        musicDetails = (List<MusicDetail>) getIntent().getSerializableExtra("musicList");

        pbMusic.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {
                if (fromUser)

                    mMediaPlayer.seekTo((int) (mMediaPlayer.getDuration() * (progress / 100.0)));
            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {

            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {

            }
        });


        listOrderType = ListOrderType.valueOf(sharedPreferences.getString("listOrderType", ListOrderType.ONE_REPEAT.name()));
        if (listOrderType == ListOrderType.ONE_REPEAT) {
            imgRepeat.setImageResource(R.drawable.ic_repeat_one_black_24dp);
        } else if (listOrderType == ListOrderType.REPEAT) {
            imgRepeat.setImageResource(R.drawable.ic_repeat_black_24dp);
        } else if (listOrderType == ListOrderType.SHUFFLE) {
            imgRepeat.setImageResource(R.drawable.ic_shuffle_black_24dp);
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (listOrderType == ListOrderType.REPEAT) {
                    if (positionToPlay == musicDetails.size() - 1)
                        positionToPlay = 0;
                    else positionToPlay++;

                } else if (listOrderType == ListOrderType.SHUFFLE) {
                    Random random = new Random(System.currentTimeMillis());
                    int temp = random.nextInt(musicDetails.size());
                    while (temp == positionToPlay) {
                        temp = random.nextInt(musicDetails.size());
                    }
                    positionToPlay = temp;
                }

                try {
                    playMusic((positionToPlay));
                } catch (IOException e) {

                    Timber.e(e);
                }

            }
        });

        if (getIntent().getExtras().containsKey("currentPosition")) {
            positionToPlay = getIntent().getIntExtra("currentPosition", 0);

            try {
                playMusic((positionToPlay));
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            } catch (IOException e) {
                Timber.d(e);
            }
        } else {
            positionToPlay = (int) YasTestApplication.getCurrentMusicId();
            initPage(positionToPlay);
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);

        }
    }


    private void initPage(int index) {
        mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                pbMusic.setValueWithNoAnimation((float) (100 * (1.0 * mMediaPlayer.getCurrentPosition() / mMediaPlayer.getDuration())));
                mSeekbarUpdateHandler.postDelayed(this, 50);
            }
        };

        MusicDetail musicDetail = musicDetails.get(index);

        tvArtist.setText(musicDetail.artist);
        tvMusicName.setText(musicDetail.title);

        getImage(musicDetail.imagePath);
        if (mMediaPlayer.isPlaying())
            imgPlay.setImageResource(R.drawable.ic_pause_black_24dp);
        else
            imgPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    private void getImage(final String imagePath) {
        picasso.load(imagePath).noFade().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                pbMusic.setImageBitmap(bitmap);

                Blurry.with(MusicPlayerActivity.this).radius(10)
                        .sampling(8)
                        .async().from(bitmap).into(imgFill);

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                getImage(imagePath);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


    }


    private void playMusic(int index) throws IOException {
        MusicDetail musicDetail = musicDetails.get(index);
        mMediaPlayer.reset();

        mMediaPlayer.setDataSource(musicDetail.musicPath);
        //mMediaPlayer.setLooping(true);
        mMediaPlayer.prepare();

        YasTestApplication.setCurrentMusicPlay(index);
        mMediaPlayer.start();
        initPage(index);
    }


//    private void initToolbar() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }


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
        unbindService(this);
        mPresenter.unsubscribe();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder b = (MusicService.MyBinder) iBinder;
        mMediaPlayer = b.getService().mediaPlayer;
        initMusic();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
