package com.yas.features.musicPlayer;

import com.yas.pojo.MusicDetail;
import com.yas.utils.bases.BasePresenter;
import com.yas.utils.bases.BaseView;

import java.util.List;

public class MusicPlayerContract {
    public interface View extends BaseView<Presenter> {
        void showError();


        void showMusics(List<MusicDetail> musicDetailList);
    }

    public interface Presenter extends BasePresenter<MusicPlayerContract.View> {

    }
}
