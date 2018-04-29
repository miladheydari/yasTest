package com.yas.di.common;


import com.yas.interactors.remote.MyApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    @Singleton
    public static MyApi provideApiService(@Named("myApiRetrofit") Retrofit retrofit) {
        return retrofit.create(MyApi.class);
    }

}
