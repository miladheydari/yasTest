package com.yas.di.common;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {
    @Singleton
    @Provides
    public static Gson provideGson() {
        return new Gson();
    }
}
