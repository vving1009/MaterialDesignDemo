package com.vving.app.materialdesigndemo.base;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by VV on 2017/10/30.
 */

public interface IBasePresenter {

    void init();

    void detachView();

    void addDisposable(DisposableObserver disposableObserver);

    void removeDisposable(DisposableObserver disposableObserver);

    void clearDisposable();
}
