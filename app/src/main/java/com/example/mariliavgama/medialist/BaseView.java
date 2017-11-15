package com.example.mariliavgama.medialist;

/**
 * Architecture style based on: https://github.com/googlesamples/android-architecture
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

}
