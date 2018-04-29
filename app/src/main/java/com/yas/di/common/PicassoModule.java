package com.yas.di.common;

import android.content.Context;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by milad on 4/28/2018.
 */
@Module
public class PicassoModule {

    @Provides
    Picasso picassoProvide(Context context, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(context).downloader(okHttp3Downloader).build();
    }

    @Provides
    public OkHttp3Downloader downloader(OkHttpClient client) {
        return new OkHttp3Downloader(client);
    }
}
