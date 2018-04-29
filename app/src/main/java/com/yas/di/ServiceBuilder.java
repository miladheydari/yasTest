package com.yas.di;

import com.yas.interactors.MusicService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by milad on 4/29/2018.
 */

@Module
public abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract MusicService bindMusicService();

}
