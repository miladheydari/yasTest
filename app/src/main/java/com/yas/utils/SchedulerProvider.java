package com.yas.utils;


import io.reactivex.Scheduler;


public interface SchedulerProvider {

    Scheduler mainThread();

    Scheduler backgroundThread();

}
