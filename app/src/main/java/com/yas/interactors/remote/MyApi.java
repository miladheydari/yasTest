package com.yas.interactors.remote;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;


public interface MyApi {
    //http://moviesapi.ir/api/v1/movies
    @GET("/api/v1/movies")
    Observable<ResponseBody> getNavigationDrawerItems();
}
