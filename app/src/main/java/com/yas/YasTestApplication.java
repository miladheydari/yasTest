package com.yas;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;

import com.yas.di.ApplicationComponent;
import com.yas.di.DaggerApplicationComponent;
import com.yas.pojo.MusicDetail;
import com.yas.utils.ListOrderType;
import com.yas.utils.bases.NoOpHasSupportFragmentInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
// helmamvp-needle-import-dagger-fragmentinjector

public class YasTestApplication extends Application implements HasActivityInjector, HasServiceInjector, HasFragmentInjector, NoOpHasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    private static long currentMusicId = -1;
    private static ListOrderType listOrderType;

    public static void setListOrderType(ListOrderType listOrderType) {
        YasTestApplication.listOrderType = listOrderType;
    }

    public static ListOrderType getListOrderType() {
        return listOrderType;
    }

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

    public static void setCurrentMusicPlay(long currentMusicId) {
        YasTestApplication.currentMusicId = currentMusicId;
    }

    public static long getCurrentMusicId() {
        return currentMusicId;
    }

    public static ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return dispatchingFragmentInjector;
    }

    // helmamvp-needle-add-dagger-fragmentinjector


}
