/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yas.features.musicPlayer;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.yas.interactors.MyInteractor;
import com.yas.interactors.remote.error.GeneralApiException;
import com.yas.pojo.MusicDetail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class MusicPlayerPresenter implements MusicPlayerContract.Presenter {

    private MyInteractor myInteractor;
    private CompositeDisposable mSubscriptions;
    private MusicPlayerContract.View mtView;
    private ContentResolver contentResolver;
    private List<MusicDetail> musicDetails;

    @Inject
    public MusicPlayerPresenter(MyInteractor myInteractor, ContentResolver contentResolver) {
        this.myInteractor = myInteractor;
        this.contentResolver = contentResolver;
        mSubscriptions = new CompositeDisposable();
    }


    public void getVideoDetailById() {
        Disposable subscription =
                myInteractor.getNavigationDrawerItems()
                        .subscribeWith(new DisposableObserver<ResponseBody>() {
                            @Override
                            public void onComplete() {
                                Timber.d("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e("onError");
                                e.printStackTrace();
                                if (e instanceof GeneralApiException) {
                                    GeneralApiException generalApiException = (GeneralApiException) e;
                                    Timber.e(generalApiException.message());
                                }
                            }

                            @Override
                            public void onNext(ResponseBody s) {
                                Timber.d("onNext");
                            }
                        });
        mSubscriptions.add(subscription);
    }

    public void getMusicList() {
        if (musicDetails == null)
            musicDetails = new ArrayList<>();
        final Cursor mCursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST
                        , MediaStore.Audio.Media.ALBUM_ID}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();

        if (mCursor.moveToFirst()) {
            do {
                MusicDetail music = new MusicDetail();

                music.title = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                music.musicPath = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                music.artist = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri,
                        Long.parseLong(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));
                music.imagePath = uri.toString();
                musicDetails.add(music);
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        mtView.showMusics(musicDetails);
    }


    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void onViewAttached(MusicPlayerContract.View view) {
        this.mtView = (view);
    }

}
