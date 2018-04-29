package com.yas.features.musicPlayer;

import dagger.Module;
import dagger.Provides;


@Module
public class MusicPlayerPresenterModule {

    @Provides
    public MusicPlayerContract.Presenter providePresenter(MusicPlayerPresenter presenter) {
        return presenter;
    }
}
