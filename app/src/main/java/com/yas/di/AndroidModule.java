package com.yas.di;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.yas.YasTestApplication;

@Module
public class AndroidModule {
    private static final int PRIVATE_MODE = 0;

    @Provides
    @Singleton
    public static Context provideContext(YasTestApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public static Resources provideResources(YasTestApplication application) {
        return application.getResources();

    }
    @Provides
    @Singleton
    public static MediaPlayer provideMediaPlayer() {
        return new MediaPlayer();

    }

    @Provides
    @Singleton
    public static ContentResolver provideContentResolver(YasTestApplication application) {
        return application.getContentResolver();

    }

    @Provides
    @Singleton
    public static SharedPreferences provideSharedPreferences(YasTestApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Named("UserSharedPref")
    @Singleton
    public static SharedPreferences provideUserSharedPreferences(YasTestApplication application) {
        return application.getSharedPreferences("UserSettings", PRIVATE_MODE);
    }
}
