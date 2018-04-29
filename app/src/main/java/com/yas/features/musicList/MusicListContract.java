package com.yas.features.musicList;

import com.yas.pojo.MusicDetail;
import com.yas.utils.bases.BasePresenter;
import com.yas.utils.bases.BaseView;

import java.util.List;

public class MusicListContract {
    public interface View extends BaseView<Presenter> {
        void showError();


        void showMusics(List<MusicDetail> musicDetailList);
    }

    public interface Presenter extends BasePresenter<MusicListContract.View> {
        void getVideoDetailById();

        void getMusicList();
    }
}
