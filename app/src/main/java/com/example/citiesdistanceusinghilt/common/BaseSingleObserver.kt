package com.example.citiesdistanceusinghilt.common

import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

abstract class BaseSingleObserver<T>(val compositeDisposable: CompositeDisposable) : SingleObserver<T> {
    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        EventBus.getDefault().post(BaseExceptionMapper.map(e))
        Timber.e(e)
    }
}