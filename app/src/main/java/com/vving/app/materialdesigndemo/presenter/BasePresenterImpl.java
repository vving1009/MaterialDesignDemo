package com.vving.app.materialdesigndemo.presenter;

import com.vving.app.materialdesigndemo.base.IBasePresenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by VV on 2017/10/30.
 */

public abstract class BasePresenterImpl implements IBasePresenter {
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void addDisposable(DisposableObserver disposableObserver) {
        if (mCompositeDisposable == null) {
            synchronized (BasePresenterImpl.class) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
            }
        }
        if (disposableObserver != null) {
            mCompositeDisposable.add(disposableObserver);
        }
    }

    @Override
    public void removeDisposable(DisposableObserver disposableObserver) {
        if (mCompositeDisposable != null && disposableObserver != null) {
            mCompositeDisposable.remove(disposableObserver);
        }
    }

    @Override
    public void clearDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void detachView() {
        clearDisposable();
    }
}
