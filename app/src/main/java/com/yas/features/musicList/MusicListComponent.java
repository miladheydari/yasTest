package com.yas.features.musicList;


import dagger.Subcomponent;


@Subcomponent(modules = {
        MusicListPresenterModule.class
})
public interface MusicListComponent {

    void inject(MusicListFragment taskDetailActivity);
}
