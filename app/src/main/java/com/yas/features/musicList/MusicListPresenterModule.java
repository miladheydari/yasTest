package com.yas.features.musicList;

import dagger.Module;
import dagger.Provides;


@Module
public class MusicListPresenterModule {

    @Provides
    public MusicListContract.Presenter providePresenter(MusicListPresenter presenter) {
        return presenter;
    }
}
