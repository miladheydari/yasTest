package com.yas.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

import com.yas.features.main.MainActivity;
import com.yas.features.musicList.MusicListPresenterModule;
import com.yas.features.musicPlayer.MusicPlayerActivity;
import com.yas.features.musicPlayer.MusicPlayerPresenterModule;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MusicPlayerPresenterModule.class})
    abstract MusicPlayerActivity bindMusicPlayerActivity();

    @ContributesAndroidInjector(modules = {MusicListPresenterModule.class})
    abstract MainActivity bindMainActivity();
// helmamvp-needle-add-dagger-activitycomponent

}
