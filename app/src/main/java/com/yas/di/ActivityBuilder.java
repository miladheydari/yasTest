package com.yas.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import com.yas.features.musicList.MusicListActivity;
import com.yas.features.musicList.MusicListPresenterModule;
import com.yas.features.musicPlayer.MusicPlayerActivity;
import com.yas.features.musicPlayer.MusicPlayerPresenterModule;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MusicListPresenterModule.class})
    abstract MusicListActivity bindCategoryActivity();
    @ContributesAndroidInjector(modules = {MusicPlayerPresenterModule.class})
    abstract MusicPlayerActivity bindMusicPlayerActivity();

    // helmamvp-needle-add-dagger-activitycomponent

}
