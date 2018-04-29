package com.yas.features.musicPlayer;


import dagger.Subcomponent;


@Subcomponent(modules = {
        MusicPlayerPresenterModule.class
})
public interface MusicPlayerComponent {

    void inject(MusicPlayerActivity taskDetailActivity);
}
