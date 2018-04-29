package com.yas.interactors;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import com.yas.interactors.remote.MyApiService;
import com.yas.utils.SchedulerProvider;



public class MyInteractor {

    private final MyApiService myApiService;
    private final SchedulerProvider scheduler;

    @Inject
    MyInteractor(MyApiService videoService, SchedulerProvider scheduler) {
        this.myApiService = videoService;
        this.scheduler = scheduler;
    }

    public Observable<ResponseBody> getNavigationDrawerItems() {
        return this.myApiService.getNavigationDrawerItems()
                .subscribeOn(scheduler.backgroundThread())
                .observeOn(scheduler.mainThread());
    }


    public Observable<ResponseBody> getMusic() {
        return this.myApiService.getNavigationDrawerItems()
                .subscribeOn(scheduler.backgroundThread())
                .observeOn(scheduler.mainThread());
    }
}
