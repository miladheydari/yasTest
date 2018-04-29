package com.yas.di;

import android.content.Context;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.yas.BuildConfig;
import com.yas.utils.Constants;
import com.yas.utils.SchedulerProvider;
import com.yas.utils.SchedulerProviderImpl;

@Module
public final class ApplicationModule {
    @Provides
    @Singleton
    @Named("isDebug")
    public static boolean provideIsDebug() {
        return BuildConfig.DEBUG;
    }

    @Provides
    @Singleton
    @Named("networkTimeoutInSeconds")
    public static int provideNetworkTimeoutInSeconds() {
        return 30;
    }



    @Provides
    @Singleton
    @Named("myApiBaseUrl")
    public static String provideMyEndpoint() {
        return Constants.MY_BASE_URL;
    }



    @Provides
    @Singleton
    public static SchedulerProvider provideAppScheduler() {
        return new SchedulerProviderImpl();
    }

    @Provides
    @Singleton
    @Named("cacheSize")
    public static long provideCacheSize() {
        return 10 * 1024 * 1024; // 10 MB
    }

    @Provides
    @Singleton
    @Named("cacheDir")
    public static File provideCacheDir(Context context) {
        return context.getCacheDir();
    }




//    @Provides
//    @Named("isConnected")
//    public static boolean provideIsConnected(Context context) {
//        return Utils.isConnected(context);
//    }

}
