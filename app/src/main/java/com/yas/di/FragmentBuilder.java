package com.yas.di;

import com.yas.features.musicList.MusicListFragment;
import com.yas.features.musicList.MusicListPresenterModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
// helmamvp-needle-add-import-dagger-fragmentcomponent


@Module
public abstract class FragmentBuilder {
    @ContributesAndroidInjector(modules = {MusicListPresenterModule.class})
    abstract MusicListFragment bindCategoryActivity();

    // helmamvp-needle-add-dagger-fragmentcomponent

}
