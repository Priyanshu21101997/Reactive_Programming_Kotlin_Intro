package com.example.reactive_programming_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    private val greeting:String = "Hello from greeting"
    private lateinit var mObservable: Observable<String>
    private lateinit var mObserver: Observer<String>

    // Disposables are used let say when we make api call and initiate the view then click on back button
    // before api loads. In this way our fragment/activity gets destroyed. So it causes memory leak
    // Disposables are used to dispose the subscription when an observer don't want to use the observable
    // Add it in Ondestroy of activity

    private lateinit var disposable: Disposable

    // Disposable observer

    private lateinit var mDisposableObserver: DisposableObserver<String>

    // COmposite disposable -> Used when there are multiple observers and we want to save ourself from
    // manulally disposing the disposable. So we add all to composite disposable and then clear it at end.

    var mCompositeDisposable:CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)


        mObservable = Observable.just(greeting)

        // ---------xxx--------------

        // Mulrithreading and concurrency on android

//        mObservable.subscribeOn(Schedulers.io())
//
//        mObservable.observeOn(AndroidSchedulers.mainThread())

        // ---------xxx--------------



//        mObserver = object:Observer<String>{
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//
//            override fun onNext(t: String) {
//                textView.text = t
//            }
//
//            override fun onError(e: Throwable) {
//            }
//
//            override fun onComplete() {
//            }
//
//        }

//        mObservable.subscribe(mObserver)

        // If you have more than one observers in the class we have to use disposable observers
        // for it.

        mDisposableObserver = object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                textView.text = t

            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }

        }

        // Composite disposable mein add
//        mCompositeDisposable.add(mDisposableObserver)
//
//
//        mObservable.subscribe(mDisposableObserver)


        //// Efficient coding paradigm on RxJava

        mCompositeDisposable.add(
            mObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(mDisposableObserver)
        )







    }

    override fun onDestroy() {
        super.onDestroy()
//        disposable.dispose() // Not required for disposable observer . It has built in method
//        mDisposableObserver.dispose()

        // Composite disposable
//        mCompositeDisposable.clear()
    }
}