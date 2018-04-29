package com.yas.interactors.remote;


import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.yas.interactors.remote.error.GeneralApiException;
import com.yas.pojo.MusicDetail;


@Singleton
public class MyApiService {
    private final MyApi api;

    @Inject
    public MyApiService(MyApi api) {
        this.api = api;
    }


    public Observable<ResponseBody> getNavigationDrawerItems() {
        return api.getNavigationDrawerItems()
                .compose(this.<ResponseBody>parseHttpErrors());
    }


    <T> ObservableTransformer<T, T> parseHttpErrors() {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.onErrorResumeNext(new Function<Throwable, ObservableSource<? extends T>>() {

                    @Override
                    public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof HttpException) {

                            Gson gson = new Gson();
                            GeneralApiException generalApiException = null;
                            try {
                                generalApiException = gson.fromJson(((HttpException) throwable).response().errorBody().string(), GeneralApiException.class);
                                generalApiException.code = ((HttpException) throwable).code();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (generalApiException != null)
                                return Observable.error(generalApiException);

                        }
                        // if not the kind we're interested in, then just report the same error to onError()
                        return Observable.error(throwable);
                    }
                });
            }
        };
    }

}
