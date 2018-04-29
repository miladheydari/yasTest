package com.yas.di;


import com.yas.YasTestApplication;
import com.yas.di.common.ApiModule;
import com.yas.di.common.GsonModule;
import com.yas.di.common.OkHttpInterceptorModule;
import com.yas.di.common.PicassoModule;
import com.yas.di.common.RetrofitModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        FragmentBuilder.class,
        ActivityBuilder.class,
        ServiceBuilder.class,
        AndroidModule.class,
        ApplicationModule.class,
        RetrofitModule.class,
        ApiModule.class,
        OkHttpInterceptorModule.class,
        GsonModule.class,
        PicassoModule.class
})
public interface ApplicationComponent {
    void inject(YasTestApplication application);


    @Component.Builder
    interface Builder {
        ApplicationComponent build();

        @BindsInstance
        Builder application(YasTestApplication application);
    }
}

