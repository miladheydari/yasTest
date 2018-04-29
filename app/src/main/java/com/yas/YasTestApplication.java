package com.yas;

import android.app.Activity;
import android.app.Application;

import com.yas.di.ApplicationComponent;
import com.yas.di.DaggerApplicationComponent;
import com.yas.pojo.MusicDetail;
import com.yas.utils.bases.NoOpHasSupportFragmentInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
// helmamvp-needle-import-dagger-fragmentinjector

public class YasTestApplication extends Application implements HasActivityInjector, NoOpHasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    private static MusicDetail currentMusic;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .application(this)
                .build();
        component.inject(this);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/shabnam_m.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private static ApplicationComponent component;

    public static void setCurrentMusicPlay(MusicDetail musicDetail)
    {
        currentMusic=musicDetail;
    }

    public static MusicDetail getCurrentMusic() {
        return currentMusic;
    }

    public static ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    // helmamvp-needle-add-dagger-fragmentinjector


}
